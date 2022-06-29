package antihackerman.service;

import antihackerman.dto.ReportDeviceDTO;
import antihackerman.exceptions.NotFoundException;
import antihackerman.logs.Log;
import antihackerman.messaging.Filter;
import antihackerman.messaging.Message;
import antihackerman.messaging.MessageReader;
import antihackerman.model.Device;
import antihackerman.model.Group;
import antihackerman.model.RealEstate;
import antihackerman.model.User;
import antihackerman.repository.DeviceRepository;
import antihackerman.repository.UserRepository;
import antihackerman.util.FilterUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private KieContainer kContainer;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private LogService logService;

    @EventListener(ApplicationReadyEvent.class)
    public void readMessages() {

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String separator = System.getProperty("file.separator");
        String devicesFolder = s + separator + "src" + separator + "main" + separator + "resources"
                + separator + "devices" + separator;
        List<Device> devices = deviceRepository.findAll();
        for (Device d: devices) {
            Runnable runnable = new MessageReader(d, devicesFolder,kContainer,notificationService,logService);
            new Thread(runnable).start();
        }
    }

    public List<Message> getMessagesForDevice(Integer id) throws NotFoundException {
        Device device = deviceRepository.getById(id);
        if(device == null){
            throw new NotFoundException("Device with id " + id + " not found.");
        }

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String separator = System.getProperty("file.separator");
        String devicesFolder = s + separator + "src" + separator + "main" + separator + "resources"
                + separator + "devices" + separator;

        List<Message> result = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(devicesFolder + device.getFilePath()))
        {
            Object obj = jsonParser.parse(reader);

            JSONArray messagesList = (JSONArray) obj;

            for (Object o: messagesList) {
                JSONObject msg = (JSONObject) o;

                Filter f = FilterUtil.createFilter(device.getFilter());
                if (f==null){
                    throw new InvalidPropertiesFormatException("Filter not good.");
                }else if (!FilterUtil.passedFilter(f, msg, device.getType())){
                    continue;
                }

                Message message = Message.builder()
                        .message(String.valueOf(msg.get("message")))
                        .value((Long)msg.get("value"))
                        .timestamp(new Timestamp(Double.valueOf((Double) msg.get("timestamp")).longValue()))
                        .build();
                result.add(message);


            }
            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<Device> getAllDevicesForUser(String username) throws NotFoundException {
        User user=userRepository.findOneByUsername(username);
        if (user == null){
            throw new NotFoundException("User with username " + username + " not found.");
        }

        Set<Device> result = new HashSet<>();
        for (RealEstate re: user.getRealestatesTenanting()) {
            result.addAll(re.getDevices());
        }

        for (Group g: user.getGroupsOwning()) {
            for (RealEstate re: g.getRealEstates()) {
                result.addAll(re.getDevices());
            }
        }

        return result;
    }
    
    public HashMap<Integer, ReportDeviceDTO> formReport(String username,String start,String end) throws NotFoundException {
		
		Set<Device> devices=getAllDevicesForUser(username);
		List<Log> logs=logService.getDeviceLogs(start,end);
		HashMap<Integer, ReportDeviceDTO> dtos=new HashMap<Integer, ReportDeviceDTO>();
		
		for(Log l:logs) {
			Optional<Device> device=devices.stream().filter(d -> d.getId().equals(Integer.parseInt(l.getUsername()))).findAny();
			if(device.isPresent()) {
				if(dtos.containsKey(device.get().getId())) {
					dtos.get(device.get().getId()).increaseNumber();
				}else {
					dtos.put(device.get().getId(), new ReportDeviceDTO(device.get().getName(), device.get().getRealestate().getName(), device.get().getRealestate().getGroup().getName(), device.get().getId(), 1));
				}
			}
					
		}
		
		return dtos;
    	
    }
}
