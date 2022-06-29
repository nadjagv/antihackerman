package antihackerman.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import antihackerman.model.DeviceAlarm;
import antihackerman.model.LogAlarm;
import antihackerman.repository.LogAlarmRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.logs.Log;
import antihackerman.logs.LogType;
import antihackerman.logs.LogsRepository;


@Service
public class LogService {
	
	@Autowired
	private LogsRepository logsRepository;
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private LogAlarmRepository logAlarmRepository;

	@Autowired
	private KieContainer kieContainer;

	
	
	public void createLog(LogType type,String username,String ipAddress,String message) {
		Log log=new Log(type, username, ipAddress, message,false);
        logsRepository.insert(log);
        //fire alarms

        if(type==LogType.ERROR) {
        	notificationService.simpleNotification(message);
        }
		List<LogAlarm> allAlarms = logAlarmRepository.findAll();
		Set<LogAlarm> activated = new HashSet<>();
		for (LogAlarm la: allAlarms) {
			Integer numConditions = la.getConditionsToSatisfy();

			if (la.getLogType() != null){
				if (la.getLogType().equals(log.getType())){
					numConditions--;

				}
			}

			if (la.getUsername()!= null && !la.getUsername().isEmpty()){
				if (message.contains(la.getUsername())){
					numConditions--;
				}
			}

			if (la.getCharSequence()!= null && !la.getCharSequence().isEmpty()){
				if (message.contains(la.getCharSequence())){
					numConditions--;
				}
			}

			if (numConditions==0){
				activated.add(la);
			}

		}
		for (LogAlarm la : activated) {
			System.out.println("Alarm: " + la.getName() + " activated: " + message);
			notificationService.simpleNotification("Alarm: " + la.getName() + " activated: " + message);
		}

	}
	
	public void createDeviceLog(Integer deviceId,String message) {
		Log log=new Log(LogType.ERROR, deviceId.toString(), "", message,true);
        logsRepository.insert(log);
	}

}
