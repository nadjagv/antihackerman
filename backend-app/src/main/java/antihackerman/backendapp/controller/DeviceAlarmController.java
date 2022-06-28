package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.DeviceAlarmDTO;
import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceAlarm;
import antihackerman.backendapp.service.DeviceAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/device-alarms")
public class DeviceAlarmController {

    @Autowired
    DeviceAlarmService deviceAlarmService;

    @PostMapping()
    @PreAuthorize("hasAuthority('CRUD_DEVICE_ALARM')")
    public ResponseEntity<DeviceAlarmDTO> createDeviceAlarm(@RequestBody DeviceAlarmDTO dto){

        try {
            DeviceAlarm deviceAlarm = deviceAlarmService.createDeviceAlarm(dto);
            return new ResponseEntity<DeviceAlarmDTO>(new DeviceAlarmDTO(deviceAlarm), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceAlarmDTO>(new DeviceAlarmDTO(), HttpStatus.NOT_FOUND);
        }

    }
}
