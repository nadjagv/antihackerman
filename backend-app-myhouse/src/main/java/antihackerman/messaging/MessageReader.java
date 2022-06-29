package antihackerman.messaging;

import antihackerman.exceptions.NotFoundException;
import antihackerman.model.*;
import antihackerman.repository.DeviceRepository;
import antihackerman.service.DeviceService;
import antihackerman.service.LogService;
import antihackerman.service.NotificationService;
import antihackerman.util.FilterUtil;
import lombok.SneakyThrows;
import org.bson.json.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;

public class MessageReader implements Runnable{
    private Device device;
    private String devicesDirPath;
    
    private final KieContainer kieContainer;
    
    private NotificationService notificationService;
    private LogService logService;

    public MessageReader(Device device, String path,KieContainer kieContainer,NotificationService notificationService,LogService logService){
        this.device = device;
        this.devicesDirPath = path;
        this.kieContainer=kieContainer;
        this.notificationService=notificationService;
        this.logService=logService;
    }

    @SneakyThrows
    @Override
    public void run() {
        Double lastMsgTimestampMils = 0.0;


        while(true){
            JSONParser jsonParser = new JSONParser();
            


            try (FileReader reader = new FileReader(this.devicesDirPath + this.device.getFilePath()))
            {
                Object obj = jsonParser.parse(reader);

                JSONArray messagesList = (JSONArray) obj;

                for (Object o: messagesList) {
                    JSONObject msg = (JSONObject) o;

                    if ((Double)msg.get("timestamp") <= lastMsgTimestampMils){
                        continue;
                    }
                    if (!this.device.getFilter().isEmpty()){
                        Filter f = FilterUtil.createFilter(this.device.getFilter());
                        if (f==null){
                            throw new InvalidPropertiesFormatException("Filter not good.");
                        }else if (!FilterUtil.passedFilter(f, msg, this.device.getType())){
                            continue;
                        }
                    }



                    //proveravanje alarma, prosla je filter

                    ArrayList<DeviceAlarm> activatedAlarms = new ArrayList<>();
                    Message message = Message.builder()
                            .message(String.valueOf(msg.get("message")))
                            .value((Long)msg.get("value"))
                            .timestamp(new Timestamp(Double.valueOf((Double) msg.get("timestamp")).longValue()))
                            .build();
                    KieSession kieSession = kieContainer.newKieSession();
                    kieSession.insert(this.device);
                    kieSession.insert(message);
                    for (DeviceAlarm da: this.device.getAlarms()) {
                        kieSession.insert(da);
                    }
                    kieSession.setGlobal("activatedAlarms", activatedAlarms);
                    kieSession.getAgenda().getAgendaGroup("deviceGroup").setFocus();

                    kieSession.fireAllRules();
                    kieSession.dispose();
                    
                    System.out.println(msg.get("message"));
                    System.out.println("Alarms activated: "+ activatedAlarms.size());

                    
                    for(DeviceAlarm da: activatedAlarms) {
                    	for(User u:da.getDevice().getRealestate().getTenants()) {
                    		notificationService.userNotification(msg.get("message").toString(), u.getUsername());
                    	}
                    	for(User u:da.getDevice().getRealestate().getGroup().getOwners()) {
                    		notificationService.userNotification(msg.get("message").toString(), u.getUsername());
                    	}
                    	
                    	logService.createDeviceLog(da.getDevice().getId(), msg.get("message").toString());
                    }

                }

                JSONObject lastMsg = (JSONObject) messagesList.get(messagesList.size()-1);
                lastMsgTimestampMils = (Double) lastMsg.get("timestamp");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Thread.sleep(this.device.getReadIntervalMils());
        }
    }



}
