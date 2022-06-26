package antihackerman.backendapp.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
@Inheritance(strategy=InheritanceType.JOINED)
@SQLDelete(sql
        = "UPDATE devices "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "filter", nullable = false)
    private String filter;

    @Column(name = "read_interval_mils", nullable = false)
    private Integer readIntervalMils;

    @Column(name = "type", nullable = false)
    private DeviceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private RealEstate realEstate;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    private Set<DeviceAlarm> alarms=new HashSet<>();


}
