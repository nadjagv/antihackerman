package antihackerman.service;

import antihackerman.exceptions.NotFoundException;
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

    @EventListener(ApplicationReadyEvent.class)
    public void readMessages() {

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String separator = System.getProperty("file.separator");
        String devicesFolder = s + separator + "src" + separator + "main" + separator + "resources"
                + separator + "devices" + separator;
        List<Device> devices = deviceRepository.findAll();
        for (Device d: devices) {
            Runnable runnable = new MessageReader(d, devicesFolder,kContainer,notificationService);
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

    public Set<Device> getAllDevicesForUser(Integer userId) throws NotFoundException {
        User user = userRepository.getById(userId);
        if (user == null){
            throw new NotFoundException("User with id " + userId + " not found.");
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
}
