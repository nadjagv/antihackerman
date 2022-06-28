package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.DeviceAlarmDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceAlarm;
import antihackerman.backendapp.repository.DeviceAlarmRepository;
import antihackerman.backendapp.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceAlarmService {
    @Autowired
    private DeviceAlarmRepository deviceAlarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceAlarm createDeviceAlarm(DeviceAlarmDTO dto) throws NotFoundException {
        Device device = deviceRepository.getById(dto.getDeviceId());
        if (device == null){
            throw new NotFoundException("Device with id " + dto.getDeviceId() + " not found.");
        }

        //TODO validacija

        if (dto.getBorderMin() == null){
            dto.setBorderMin(Long.MIN_VALUE);
        }

        if (dto.getBorderMax() == null){
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
}
