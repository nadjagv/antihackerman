package antihackerman.backendapp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	public List<Log> findAll(){
		return logsRepository.findAll();
	}
	
	public List<Log> find(LogType type,String user){
		return logsRepository.findLogs(type,user);
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

}
