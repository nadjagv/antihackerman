package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.DeviceConfigDTO;
import antihackerman.backendapp.dto.DeviceDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.*;
import antihackerman.backendapp.repository.BooleanDeviceRepository;
import antihackerman.backendapp.repository.DeviceRepository;
import antihackerman.backendapp.repository.IntervalDeviceRepository;
import antihackerman.backendapp.repository.RealEstateRepository;
import antihackerman.backendapp.util.FileUtil;
import antihackerman.backendapp.util.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        Optional<IntervalDevice> ind = intervalDeviceRepository.findById(id);
        Optional<BooleanDevice> bd = booleanDeviceRepository.findById(id);
        Device device = deviceRepository.getById(id);
        if (ind.isPresent()) {
            return ind.get();
        } else if (bd.isPresent()) {
            return bd.get();
        } else {
            throw new NotFoundException("Device with id " + id + " not found.");
        }
    }

    public Device addDevice(DeviceDTO dto) throws NotFoundException, InvalidInputException {

        RealEstate realEstate = realEstateRepository.getById(dto.getRealestateId());
        if (realEstate == null){
            throw new NotFoundException("Real Estate with id " + dto.getRealestateId() + " not found.");
        }

        dto.setFilter(FilterUtil.trimAndLower(dto.getFilter()));
        if (!FilterUtil.isTrue(dto.getFilter())
                && !FilterUtil.isFalse(dto.getFilter())
                && !FilterUtil.isEqual(dto.getFilter())
                && !FilterUtil.isLess(dto.getFilter())
                && !FilterUtil.isGreater(dto.getFilter())
                && !dto.getFilter().isEmpty()){
            throw new InvalidInputException("Inavlid filter.");
        }

        if (dto.getName() == null || dto.getFilePath()==null || dto.getReadIntervalMils()==null|| dto.getType()==null){
            throw new InvalidInputException("Missing input.");
        }

        if (dto.getType().equals(DeviceType.BOOLEAN_DEVICE)){
            if (dto.getActiveFalseStr() == null || dto.getActiveTrueStr()==null){
                throw new InvalidInputException("Missing input.");
            }
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
            if (dto.getMaxValue() == null || dto.getMinValue()==null || dto.getValueDefinition()==null){
                throw new InvalidInputException("Missing input.");
            }
            IntervalDevice device = IntervalDevice.builder()
                    .deleted(false)
                    .name(dto.getName())
                    .alarms(new HashSet<>())
                    .filter(dto.getFilter())
                    .filePath(dto.getFilePath())
                    .realestate(realEstate)
                    .readIntervalMils(dto.getReadIntervalMils())
                    .type(DeviceType.INTERVAL_DEVICE)
                    .valueDefinition(dto.getValueDefinition())
                    .maxValue(dto.getMaxValue())
                    .minValue(dto.getMinValue())
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
