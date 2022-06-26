package antihackerman.backendapp.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "device_alarms")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeviceAlarm {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alarm_for_bool")
    private boolean alarmForBool;

    @Column(name = "border_min")
    private Double borderMin;

    @Column(name = "border_max")
    private Double borderMax;

    @Column(name = "activation_count", nullable = false)
    private Integer activationCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;

    public DeviceAlarm() {
        this.activationCount = 0;
    }
}
