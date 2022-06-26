package antihackerman.backendapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.*;

import javax.persistence.Column;

@Entity
@Table(name = "boolean_devices")
@SQLDelete(sql
        = "UPDATE boolean_devices "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class BooleanDevice extends Device{
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "active_true_str", nullable = false)
    private String activeTrueStr;

    @Column(name = "active_false_str", nullable = false)
    private String activeFalseStr;

    @Column(name = "type", nullable = false)
    private BooleanDeviceType type;

    @Builder
    public BooleanDevice(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, RealEstate realEstate, boolean active, String activeTrueStr, String activeFalseStr, BooleanDeviceType type) {
        super(id, deleted, name, filePath, filter, readIntervalMils, realEstate);
        this.active = active;
        this.activeTrueStr = activeTrueStr;
        this.activeFalseStr = activeFalseStr;
        this.type = type;
    }
}
