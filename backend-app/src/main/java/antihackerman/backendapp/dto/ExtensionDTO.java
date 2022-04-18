package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.ExtensionType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionDTO {
    private ExtensionType type;
    private boolean critical;
}
