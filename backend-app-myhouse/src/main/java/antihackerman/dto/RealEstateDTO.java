package antihackerman.dto;

import antihackerman.model.RealEstate;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateDTO {

    private Integer id, groupId;
    private String name, location;

    public RealEstateDTO(RealEstate realEstate){
        this.id = realEstate.getId();
        this.name = realEstate.getName();
        this.location = realEstate.getLocation();
        this.groupId = realEstate.getGroup().getId();
    }
}
