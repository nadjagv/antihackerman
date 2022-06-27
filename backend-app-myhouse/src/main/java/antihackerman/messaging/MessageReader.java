package antihackerman.messaging;

import antihackerman.model.Device;
import antihackerman.model.DeviceType;
import antihackerman.repository.DeviceRepository;
import antihackerman.util.FilterUtil;
import lombok.SneakyThrows;
import org.bson.json.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import java.util.InvalidPropertiesFormatException;

public class MessageReader implements Runnable{
    private Device device;
    private String devicesDirPath;

    @Autowired
    DeviceRepository deviceRepository;

    public MessageReader(Device device, String path){
        this.device = device;
        this.devicesDirPath = path;
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

                    Filter f = FilterUtil.createFilter(this.device.getFilter());
                    if (f==null){
                        throw new InvalidPropertiesFormatException("Filter not good.");
                    }else if (!FilterUtil.passedFilter(f, msg, this.device.getType())){
                        continue;
                    }

                    //TODO: proveravanje alarma, prosla je filter
                    
                    System.out.println(msg.get("message"));

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
