package antihackerman.backendapp.logs;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import antihackerman.backendapp.model.BlacklistedJWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("logs")
public class Log {
	
	@Id
    private String id;
	
	private String desc;
	
	private LogType type;
	
	private LocalDateTime timestamp;
	
	private String username;
	
	private String ip;
	
	public Log(LogType type,String username,String ip,String desc) {
		this.ip=ip;
		this.type=type;
		this.desc=desc;
		this.username=username;
		this.timestamp=LocalDateTime.now();
	}
	

}
