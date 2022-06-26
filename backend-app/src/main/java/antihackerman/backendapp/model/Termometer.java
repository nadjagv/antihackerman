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
@Table(name = "termometers")
@SQLDelete(sql
        = "UPDATE termometers "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class Termometer extends Device{
    @Column(name = "max_value", nullable = false)
    private Double maxValue;

    @Column(name = "min_value", nullable = false)
    private Double minValue;

    @Builder

    public Termometer(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, RealEstate realEstate, Double maxValue, Double minValue) {
        super(id, deleted, name, filePath, filter, readIntervalMils, realEstate);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }
}
