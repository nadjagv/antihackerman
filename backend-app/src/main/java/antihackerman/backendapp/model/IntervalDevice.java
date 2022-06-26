package antihackerman.backendapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "interval_devices")
@SQLDelete(sql
        = "UPDATE interval_devices "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class IntervalDevice extends Device{
    @Column(name = "value_definition", nullable = false)
    private String valueDefinition;

    @Builder
    public IntervalDevice(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, DeviceType type, RealEstate realEstate, Set<DeviceAlarm> alarms, String valueDefinition) {
        super(id, deleted, name, filePath, filter, readIntervalMils, type, realEstate, alarms);
        this.valueDefinition = valueDefinition;
    }
}
