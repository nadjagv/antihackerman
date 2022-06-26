package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.Device;
import antihackerman.backendapp.model.RealEstate;
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
        System.out.println("eeeeeeeeeee re dto \n\n");
        for (Device d: realEstate.getDevices()) {
            devices.add(new DeviceDTO(d));
        }
    }
}
