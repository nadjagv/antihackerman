package antihackerman.dto;

import antihackerman.model.Device;
import antihackerman.model.RealEstate;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateDTO {

    private Integer id, groupId;
    private String name, location;
    private List<DeviceDTO> devices;

    public RealEstateDTO(RealEstate realEstate){
        this.id = realEstate.getId();
        this.name = realEstate.getName();
        this.location = realEstate.getLocation();
        this.groupId = realEstate.getGroup().getId();

        this.devices = new ArrayList<>();
        for (Device d: realEstate.getDevices()) {
            devices.add(new DeviceDTO(d));
        }
    }
}
