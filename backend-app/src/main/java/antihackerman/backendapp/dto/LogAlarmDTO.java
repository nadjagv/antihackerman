package antihackerman.backendapp.dto;

import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.LogAlarm;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogAlarmDTO {

    private Integer id;
    private boolean deleted;
    private String name;
    private LogType logType;
    private String username;
    private String charSequence;

    public LogAlarmDTO(LogAlarm la){
        this.id = la.getId();
        this.deleted = la.isDeleted();
        this.name = la.getName();
        this.logType = la.getLogType();
        this.username = la.getUsername();
        this.charSequence = la.getCharSequence();
    }
}
