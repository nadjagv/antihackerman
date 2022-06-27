package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.DeviceConfigDTO;
import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.*;
import antihackerman.backendapp.repository.BooleanDeviceRepository;
import antihackerman.backendapp.repository.DeviceRepository;
import antihackerman.backendapp.repository.IntervalDeviceRepository;
import antihackerman.backendapp.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BooleanDeviceRepository booleanDeviceRepository;

    @Autowired
    private IntervalDeviceRepository intervalDeviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    public Device getById(Integer id) throws NotFoundException {
        Device device = deviceRepository.getById(id);
        if (device == null){
            throw new NotFoundException("Device with id " + id + " not found.");
        }
        return device;
    }

    public Device addDevice(DeviceDTO dto) throws NotFoundException {

        RealEstate realEstate = realEstateRepository.getById(dto.getRealestateId());
        if (realEstate == null){
            throw new NotFoundException("Real Estate with id " + dto.getRealestateId() + " not found.");
        }

        if (dto.getType().equals(DeviceType.BOOLEAN_DEVICE)){
            BooleanDevice device = BooleanDevice.builder()
                    .deleted(false)
                    .name(dto.getName())
                    .alarms(new HashSet<>())
                    .filter(dto.getFilter())
                    .filePath(dto.getFilePath())
                    .realEstate(realEstate)
                    .readIntervalMils(dto.getReadIntervalMils())
                    .type(DeviceType.BOOLEAN_DEVICE)
                    .activeFalseStr(dto.getActiveFalseStr())
                    .activeTrueStr(dto.getActiveTrueStr())
                    .build();
            return booleanDeviceRepository.save(device);
        }else{
            IntervalDevice device = IntervalDevice.builder()
                    .deleted(false)
                    .name(dto.getName())
                    .alarms(new HashSet<>())
                    .filter(dto.getFilter())
                    .filePath(dto.getFilePath())
                    .realEstate(realEstate)
                    .readIntervalMils(dto.getReadIntervalMils())
                    .type(DeviceType.INTERVAL_DEVICE)
                    .valueDefinition(dto.getValueDefinition())
                    .build();
            return intervalDeviceRepository.save(device);
        }

    }

    public void deleteDevice(Integer id) throws NotFoundException {

        Device device = deviceRepository.getById(id);
        if (device == null){
            throw new NotFoundException("Device with id " + id + " not found.");
        }

        deviceRepository.delete(device);

    }
    
    public void createConfigFile() {
    	List<BooleanDevice> booleanDevices=booleanDeviceRepository.findAll();
    	List<IntervalDevice> intervalDevices=intervalDeviceRepository.findAll();
    	
    	List<DeviceConfigDTO> devices=new ArrayList<DeviceConfigDTO>();
    	
    	for(BooleanDevice bd: booleanDevices) {
    		devices.add(new DeviceConfigDTO(bd));
    	}
    	
    	for(IntervalDevice id: intervalDevices) {
    		devices.add(new DeviceConfigDTO(id));
    	}
    	
    	String path = "./src/main/resources/device_config.json";
    	 
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(path), devices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
