package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.DeviceAlarmDTO;
import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceAlarm;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.service.DeviceAlarmService;
import antihackerman.backendapp.service.LogService;
import antihackerman.backendapp.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/device-alarms")
public class DeviceAlarmController {

    @Autowired
    DeviceAlarmService deviceAlarmService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAuthority('CRUD_DEVICE_ALARM')")
    public ResponseEntity<ArrayList<DeviceAlarmDTO>> getAll(@RequestHeader (name="Authorization") String token, HttpServletRequest request, @PathVariable Integer deviceId){

        try {
            Set<DeviceAlarm> deviceAlarms = deviceAlarmService.getAlarmsForDevice(deviceId);
            ArrayList<DeviceAlarmDTO> dtos = new ArrayList<>();
            for (DeviceAlarm g: deviceAlarms) {
                dtos.add(new DeviceAlarmDTO(g));
            }

            String username=tokenUtils.getUsernameFromToken(token.substring(7));

            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected all alarms for device.");

            return new ResponseEntity<ArrayList<DeviceAlarmDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<DeviceAlarmDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CRUD_DEVICE_ALARM')")
    public ResponseEntity<DeviceAlarmDTO> createDeviceAlarm(@RequestBody DeviceAlarmDTO dto){

        try {
            DeviceAlarm deviceAlarm = deviceAlarmService.createDeviceAlarm(dto);
            return new ResponseEntity<DeviceAlarmDTO>(new DeviceAlarmDTO(deviceAlarm), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceAlarmDTO>(new DeviceAlarmDTO(), HttpStatus.NOT_FOUND);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceAlarmDTO>(new DeviceAlarmDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CRUD_DEVICE_ALARM')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        try {
            deviceAlarmService.deleteDeviceAlarm(id);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Device alarm not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
