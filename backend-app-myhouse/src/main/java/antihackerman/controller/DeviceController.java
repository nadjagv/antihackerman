package antihackerman.controller;

import antihackerman.logs.LogType;
import antihackerman.messaging.Message;
import antihackerman.service.DeviceService;
import antihackerman.service.LogService;
import antihackerman.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3010/" })
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/messages/{id}")
    @PreAuthorize("hasAuthority('READ_MESSAGES_USER')")
    public ResponseEntity<ArrayList<Message>> getMessagesForDevice(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            List<Message> messages = deviceService.getMessagesForDevice(id);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected messages for device.");

            return new ResponseEntity<ArrayList<Message>>((ArrayList<Message>) messages, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested messages for device not in database.");
            return new ResponseEntity<ArrayList<Message>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
