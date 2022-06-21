package antihackerman.dto;

import antihackerman.model.Group;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private Integer id;
    private String name;

    public GroupDTO(Group group){
        this.id = group.getId();
        this.name = group.getName();
    }

}
