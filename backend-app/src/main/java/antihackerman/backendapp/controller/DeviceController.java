package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.service.DeviceService;
import antihackerman.backendapp.service.GroupService;
import antihackerman.backendapp.service.LogService;
import antihackerman.backendapp.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_DEVICE')")
    public ResponseEntity<DeviceDTO> getOne(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            Device device = deviceService.getById(id);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected device by id.");
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(device), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested device by id not in database.");
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_DEVICE')")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO dto, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            Device device = deviceService.addDevice(dto);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" created device.");
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(device), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested creating device with real estate not in database.");
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.NOT_FOUND);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" requested creating device invalid data.");
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_DEVICE')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            deviceService.deleteDevice(id);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" deleted device.");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested deleting device not in database.");
            return new ResponseEntity<String>("Device not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
