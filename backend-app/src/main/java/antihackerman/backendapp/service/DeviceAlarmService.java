package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.DeviceAlarmDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceAlarm;
import antihackerman.backendapp.model.DeviceType;
import antihackerman.backendapp.repository.DeviceAlarmRepository;
import antihackerman.backendapp.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DeviceAlarmService {
    @Autowired
    private DeviceAlarmRepository deviceAlarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public Set<DeviceAlarm> getAlarmsForDevice(Integer deviceId) throws NotFoundException {
        Device device = deviceRepository.getById(deviceId);
        if (device == null){
            throw new NotFoundException("Device with id " + deviceId + " not found.");
        }
        return device.getAlarms();
    }

    public DeviceAlarm createDeviceAlarm(DeviceAlarmDTO dto) throws NotFoundException, InvalidInputException {
        Device device = deviceRepository.getById(dto.getDeviceId());
        if (device == null){
            throw new NotFoundException("Device with id " + dto.getDeviceId() + " not found.");
        }

        //validacija
        //TODO: provera da li je u granicama min max za interval
        if (device.getType().equals(DeviceType.INTERVAL_DEVICE) && dto.getBorderMin() == null && dto.getBorderMax() == null){
            throw new InvalidInputException("Invalid input for creating alarm - both values null.");
        }

        if (dto.getBorderMin() == null && device.getType().equals(DeviceType.INTERVAL_DEVICE)){
            dto.setBorderMin(Long.MIN_VALUE);
        }

        if (dto.getBorderMax() == null && device.getType().equals(DeviceType.INTERVAL_DEVICE)){
            dto.setBorderMax(Long.MAX_VALUE);
        }
        DeviceAlarm deviceAlarm = DeviceAlarm.builder()
                .name(dto.getName())
                .alarmForBool(dto.isAlarmForBool())
                .borderMin(dto.getBorderMin())
                .borderMax(dto.getBorderMax())
                .activationCount(0)
                .device(device)
                .build();

        return deviceAlarmRepository.save(deviceAlarm);

    }

    public void deleteDeviceAlarm(Integer id) throws NotFoundException {
        DeviceAlarm deviceAlarm = deviceAlarmRepository.getById(id);
        if (deviceAlarm == null){
            throw new NotFoundException("Device alarm with id " + id + " not found.");
        }

        deviceAlarmRepository.delete(deviceAlarm);
    }
}
