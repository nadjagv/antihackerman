package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.BooleanDevice;
import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.DeviceType;
import antihackerman.backendapp.model.IntervalDevice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConfigDTO {
	private Integer id;
    private String name;
    private String filePath;
    private DeviceType type;
    private String valueDefinition;
    private String activeTrueStr;
    private String activeFalseStr;
    private Integer realestateId;
    //private String realestateName;
    private Double maxValue, minValue;

    public DeviceConfigDTO(Device d){
        this.id = d.getId();
        this.name = d.getName();
        this.filePath = d.getFilePath();
        this.realestateId = d.getRealestate().getId();
        //this.realestateName=d.getRealestate().getName();
        this.type = d.getType();
        System.out.println("hihihihih device dto \n\n");
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
