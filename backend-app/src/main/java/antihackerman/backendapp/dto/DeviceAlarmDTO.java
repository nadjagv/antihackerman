package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceAlarm;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAlarmDTO {
    private Integer id;
    private String name;
    private boolean alarmForBool;
    private Long borderMin;
    private Long borderMax;
    private Integer activationCount;
    private Integer deviceId;

    public DeviceAlarmDTO(DeviceAlarm deviceAlarm){
        this.id = deviceAlarm.getId();
        this.activationCount = deviceAlarm.getActivationCount();
        this.name = deviceAlarm.getName();
        this.alarmForBool = deviceAlarm.isAlarmForBool();
        this.borderMin = deviceAlarm.getBorderMin();
        this.borderMax = deviceAlarm.getBorderMax();
        this.deviceId = deviceAlarm.getDevice().getId();

    }
}
