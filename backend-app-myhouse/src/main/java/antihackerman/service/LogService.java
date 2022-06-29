package antihackerman.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public void createLog(LogType type,String username,String ipAddress,String message) {
		Log log=new Log(type, username, ipAddress, message,false);
        logsRepository.insert(log);
        //fire alarms
        
        if(type==LogType.ERROR) {
        	notificationService.simpleNotification(message);
        }
	}
	
	public void createDeviceLog(Integer deviceId,String message) {
		Log log=new Log(LogType.ERROR, deviceId.toString(), "", message,true);
        logsRepository.insert(log);
	}
	
	public List<Log> getDeviceLogs(String start,String end){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime startLDT=null;
		LocalDateTime endLDT=null;
		if(start!=null) {
			startLDT=LocalDateTime.parse(start, formatter);
		}
		if(end!=null) {
			endLDT=LocalDateTime.parse(end, formatter);
		}
		
		List<Log> logs=logsRepository.findAll();
		List<Log> returnLogs=new ArrayList<Log>();
		for(Log l:logs) {
			if(!l.isDevice()) {
				continue;
			}
			boolean check=true;
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
        	if(check) {
        		returnLogs.add(l);
        	}
		}
		
		return returnLogs;
	}

}
