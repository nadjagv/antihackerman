package antihackerman.service;

import antihackerman.messaging.MessageReader;
import antihackerman.model.Device;
import antihackerman.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void readMessages() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String separator = System.getProperty("file.separator");
        String devicesFolder = s + separator + "src" + separator + "main" + separator + "resources"
                + separator + "devices" + separator;
        List<Device> devices = deviceRepository.findAll();
        for (Device d: devices) {
            Runnable runnable = new MessageReader(d, devicesFolder);
            new Thread(runnable).start();
        }
    }
}
