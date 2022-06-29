package antihackerman.backendapp.model;

import antihackerman.backendapp.logs.LogType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "log_alarms")
@SQLDelete(sql
        = "UPDATE log_alarms "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogAlarm {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "log_type")
    private LogType logType;

    @Column(name = "username")
    private String username;

    @Column(name = "char_sequence")
    private String charSequence;

    @Column(name = "conditions_to_satisfy")
    private Integer conditionsToSatisfy;
}
