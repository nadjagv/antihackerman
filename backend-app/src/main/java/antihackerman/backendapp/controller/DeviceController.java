package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.service.DeviceService;
import antihackerman.backendapp.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_DEVICE')")
    public ResponseEntity<DeviceDTO> getOne(@PathVariable Integer id){

        try {
            Device device = deviceService.getById(id);
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(device), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_DEVICE')")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO dto){

        try {
            Device device = deviceService.addDevice(dto);
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(device), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.NOT_FOUND);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return new ResponseEntity<DeviceDTO>(new DeviceDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_DEVICE')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        try {
            deviceService.deleteDevice(id);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Device not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
