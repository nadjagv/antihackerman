package antihackerman.dto;

import antihackerman.model.DeviceType;
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
public class ReportDeviceDTO {
	
	String deviceName,realEstateName,groupName;
	Integer deviceId,numberOfAlarms;
	
	public void increaseNumber() {
		this.numberOfAlarms+=1;
	}

}
