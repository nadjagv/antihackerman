package antihackerman.dto;

import antihackerman.model.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Integer id;
    private boolean deleted;
    private String name;
    private String filePath;
    private String filter;
    private Integer readIntervalMils;
    private DeviceType type;
    private String valueDefinition;
    private boolean active;
    private String activeTrueStr;
    private String activeFalseStr;
    private Integer realestateId;
    private Long maxValue, minValue;

    public DeviceDTO(Device d){
        this.id = d.getId();
        this.deleted = d.isDeleted();
        this.name = d.getName();
        this.filePath = d.getFilePath();
        this.filter = d.getFilter();
        this.readIntervalMils = d.getReadIntervalMils();
        this.realestateId = d.getRealestate().getId();
        this.type = d.getType();
        if (d.getType().equals(DeviceType.BOOLEAN_DEVICE)){
            BooleanDevice bd = (BooleanDevice) d;
            this.activeTrueStr = bd.getActiveTrueStr();
            this.activeFalseStr = bd.getActiveFalseStr();
        }
        else{
            IntervalDevice ind = (IntervalDevice) d;
            this.valueDefinition = ind.getValueDefinition();
            this.maxValue = ind.getMaxValue();
            this.minValue = ind.getMinValue();
        }


    }
}
