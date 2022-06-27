package antihackerman.backendapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antihackerman.backendapp.logs.Log;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.service.LogService;

@CrossOrigin(origins = {"https://localhost:3000/" })
@RestController
@RequestMapping("/logs")
public class LogController {
	
	@Autowired
	private LogService logService;
	
	@GetMapping()
    @PreAuthorize("hasAuthority('READ_LOGS')")
    public ResponseEntity<List<Log>> getLogs(@RequestParam(value="type",required=false) LogType type,
    		@RequestParam(value="start",required=false) String start,
    		@RequestParam(value="end",required=false) String end,
    		@RequestParam(value="user",required=false) String user,
    		@RequestParam(value="desc",required=false) String descContains){

        List<Log> logs=logService.findFilltered(type, start, end, user,descContains);
        
        
        return new ResponseEntity<>(logs, HttpStatus.OK);

    }
	
}
