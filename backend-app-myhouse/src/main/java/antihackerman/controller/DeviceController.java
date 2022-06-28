package antihackerman.controller;

import antihackerman.messaging.Message;
import antihackerman.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3010/" })
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/messages/{id}")
    @PreAuthorize("hasAuthority('READ_MESSAGES_USER')")
    public ResponseEntity<ArrayList<Message>> getMessagesForDevice(@PathVariable Integer id){

        try {
            List<Message> messages = deviceService.getMessagesForDevice(1);

            return new ResponseEntity<ArrayList<Message>>((ArrayList<Message>) messages, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<Message>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
