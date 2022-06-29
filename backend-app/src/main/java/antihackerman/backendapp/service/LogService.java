package antihackerman.backendapp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import antihackerman.backendapp.model.LogAlarm;
import antihackerman.backendapp.repository.LogAlarmRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import antihackerman.backendapp.logs.Log;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.logs.LogsRepository;

@Service
public class LogService {
	
	@Autowired
	private LogsRepository logsRepository;

	@Autowired
	private LogAlarmRepository logAlarmRepository;
	
	@Autowired
    private KieContainer kieContainer;
	
	@Autowired
	private NotificationService notificationService;
	
	public List<Log> findAll(){
		return logsRepository.findAll();
	}
	
	public List<Log> findFilltered(LogType type,String start,String end,String user,String descContains){
		List<Log> logs=logsRepository.findAll();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime startLDT=null;
		LocalDateTime endLDT=null;
		if(start!=null) {
			startLDT=LocalDateTime.parse(start, formatter);
		}
		if(end!=null) {
			endLDT=LocalDateTime.parse(end, formatter);
		}
		
		List<Log> logsRes=new ArrayList<Log>();
        for(Log l: logs) {
        	
        	if(l.isDevice()) {
        		continue;
        	}
        	
        	boolean check=true;
        	if(type!=null) {
        		if(l.getType()!=type) {
        			check=false;
        		}
        	}
        	if(user!=null) {
        		if(!l.getUsername().equals(user)) {
        			check=false;
        		}
        	}
        	if(startLDT!=null) {
        		if(!l.getTimestamp().isAfter(startLDT)) {
        			check=false;
        		}
        	}
        	if(endLDT!=null) {
        		if(!l.getTimestamp().isBefore(endLDT)) {
        			check=false;
        		}
        	}
        	if(descContains!=null) {
        		if(!l.getDesc().contains(descContains)) {
        			check=false;
        		}
        	}
        	if(check) {
        		logsRes.add(l);
        	}
        }
        
        return logsRes;
	}
	
	public void createLog(LogType type,String username,String ipAddress,String message) {
		Log log=new Log(type, username, ipAddress, message,false);
        logsRepository.insert(log);
        
        //fire alarms
        if(type==LogType.ERROR) {
        	notificationService.simpleNotification(message);
        }

		List<LogAlarm> allAlarms = logAlarmRepository.findAll();
		Set<LogAlarm> logTypeActivated = new HashSet<>();
		Set<LogAlarm> activated = new HashSet<>();
		for (LogAlarm la: allAlarms) {
			Integer numConditions = la.getConditionsToSatisfy();

			if (la.getLogType() != null){
				KieSession kieSession = kieContainer.newKieSession();
				kieSession.insert(la);
				kieSession.insert(log);
				kieSession.setGlobal("logTypeActivated", logTypeActivated);
				kieSession.fireAllRules();
				kieSession.dispose();

				if (!logTypeActivated.isEmpty()){
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

}
