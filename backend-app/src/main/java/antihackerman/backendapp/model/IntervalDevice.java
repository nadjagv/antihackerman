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
    @Column(name = "max_value", nullable = false)
    private Double maxValue;

    @Column(name = "min_value", nullable = false)
    private Double minValue;

    @Column(name = "type", nullable = false)
    private IntervalDeviceType type;

    @Builder

    public IntervalDevice(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, RealEstate realEstate, Double maxValue, Double minValue, IntervalDeviceType type) {
        super(id, deleted, name, filePath, filter, readIntervalMils, realEstate);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.type = type;
    }
}
