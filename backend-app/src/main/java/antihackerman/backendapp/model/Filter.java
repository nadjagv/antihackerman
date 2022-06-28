package antihackerman.backendapp.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter {
    private boolean boolValue;
    private double value;
    private double minValue;
    private double maxValue;
    private FilterType filterType;
}
