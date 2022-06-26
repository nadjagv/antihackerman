package antihackerman.backendapp.model;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "lamps")
@SQLDelete(sql
        = "UPDATE lamps "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class Lamp extends Device{
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "active_true_str", nullable = false)
    private String activeTrueStr;

    @Column(name = "active_false_str", nullable = false)
    private String activeFalseStr;

    @Builder

    public Lamp(Integer id, boolean deleted, String name, String filePath, String filter, Integer readIntervalMils, RealEstate realEstate, boolean active, String activeTrueStr, String activeFalseStr) {
        super(id, deleted, name, filePath, filter, readIntervalMils, realEstate);
        this.active = active;
        this.activeTrueStr = activeTrueStr;
        this.activeFalseStr = activeFalseStr;
    }
}
