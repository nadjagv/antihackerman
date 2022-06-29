package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.DeviceAlarmDTO;
import antihackerman.backendapp.dto.LogAlarmDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.DeviceAlarm;
import antihackerman.backendapp.model.LogAlarm;
import antihackerman.backendapp.service.LogAlarmService;
import antihackerman.backendapp.service.LogService;
import antihackerman.backendapp.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/log-alarms")
public class LogAlarmController {

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private LogAlarmService logAlarmService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('CRUD_LOG_ALARM')")
    public ResponseEntity<ArrayList<LogAlarmDTO>> getAll(@RequestHeader(name="Authorization") String token, HttpServletRequest request, @PathVariable Integer deviceId){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            List<LogAlarm> alarms = logAlarmService.getAll();
            ArrayList<LogAlarmDTO> dtos = new ArrayList<>();
            for (LogAlarm g: alarms) {
                dtos.add(new LogAlarmDTO(g));
            }

            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected all log alarms.");

            return new ResponseEntity<ArrayList<LogAlarmDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested all log alarms - exception occurred.");

            return new ResponseEntity<ArrayList<LogAlarmDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CRUD_LOG_ALARM')")
    public ResponseEntity<LogAlarmDTO> createLogAlarm(@RequestBody LogAlarmDTO dto, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            LogAlarm alarm = logAlarmService.createLogAlarm(dto);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" created log alarm.");

            return new ResponseEntity<LogAlarmDTO>(new LogAlarmDTO(alarm), HttpStatus.OK);
       } catch (InvalidInputException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" requested creating alarm invalid data.");
            return new ResponseEntity<LogAlarmDTO>(new LogAlarmDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CRUD_LOG_ALARM')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            logAlarmService.deleteLogAlarm(id);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" deleted log alarm.");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested deleting log alarm not in database.");
            return new ResponseEntity<String>("Device alarm not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
