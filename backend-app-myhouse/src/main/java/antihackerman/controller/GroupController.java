package antihackerman.controller;

import antihackerman.dto.GroupDTO;
import antihackerman.logs.LogType;
import antihackerman.model.Group;
import antihackerman.service.GroupService;
import antihackerman.service.LogService;
import antihackerman.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3010/" })
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;
    
    @Autowired
	private LogService logService;

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('READ_GROUPS_USER')")
    public ResponseEntity<ArrayList<GroupDTO>> getForUserByUsername(@PathVariable String username, HttpServletRequest request){

        try {
            Set<Group> groups = groupService.getAllForUserByUsername(username);
            ArrayList<GroupDTO> dtos = new ArrayList<>();
            for (Group g: groups) {
                dtos.add(new GroupDTO(g));
            }
            
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected his/hers groups.");
            
            return new ResponseEntity<ArrayList<GroupDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested his/hers groups but not in database.");

            return new ResponseEntity<ArrayList<GroupDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
