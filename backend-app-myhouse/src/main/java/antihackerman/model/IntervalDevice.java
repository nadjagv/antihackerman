package antihackerman.model;

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

    @Column(name = "max_value", nullable = false)
    private Double maxValue;

    @Column(name = "min_value", nullable = false)
    private Double minValue;

    @Builder
    public IntervalDevice(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, DeviceType type, RealEstate realestate, Set<DeviceAlarm> alarms, String valueDefinition, Double maxValue, Double minValue) {
        super(id, deleted, name, filePath, filter, readIntervalMils, type, realestate, alarms);
        this.valueDefinition = valueDefinition;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }
}
