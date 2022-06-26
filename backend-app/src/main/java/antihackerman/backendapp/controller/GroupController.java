package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.CSRdto;
import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.dto.UserDTO;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.logs.Log;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.logs.LogsRepository;
import antihackerman.backendapp.model.CSR;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.GroupService;
import antihackerman.backendapp.util.TokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;
    
    @Autowired
	private LogsRepository logRepository;
    
    @Autowired
	private TokenUtils tokenUtils;

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_GROUPS')")
    public ResponseEntity<ArrayList<GroupDTO>> getAll(@RequestHeader (name="Authorization") String token, HttpServletRequest request){

        try {
            List<Group> groups = groupService.getAll();
            ArrayList<GroupDTO> dtos = new ArrayList<>();
            for (Group g: groups) {
                dtos.add(new GroupDTO(g));
            }
            
            String username=tokenUtils.getUsernameFromToken(token.substring(7));
            
            Log log=new Log(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected all groups.");
            logRepository.insert(log);
            
            return new ResponseEntity<ArrayList<GroupDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<GroupDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<GroupDTO> getById(@PathVariable Integer groupId){

        try {
            Group group = groupService.getGroupById(groupId);
            return new ResponseEntity<GroupDTO>(new GroupDTO(group), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<GroupDTO>(new GroupDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/owners/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<ArrayList<UserDTO>> getOwners(@PathVariable Integer groupId){

        try {
            List<User> users = groupService.getOwnersForGroup(groupId);
            ArrayList<UserDTO> dtos = new ArrayList<>();
            for (User u: users) {
                dtos.add(new UserDTO(u));
            }
            return new ResponseEntity<ArrayList<UserDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<UserDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all-users/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<ArrayList<UserDTO>> getAllUsersForGroup(@PathVariable Integer groupId){

        try {
            List<User> users = groupService.getAllUsersForGroup(groupId);
            ArrayList<UserDTO> dtos = new ArrayList<>();
            for (User u: users) {
                dtos.add(new UserDTO(u));
            }
            return new ResponseEntity<ArrayList<UserDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<UserDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tenants/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<ArrayList<UserDTO>> getTenantsForGroup(@PathVariable Integer groupId){

        try {
            List<User> users = groupService.getTenantsForGroup(groupId);
            ArrayList<UserDTO> dtos = new ArrayList<>();
            for (User u: users) {
                dtos.add(new UserDTO(u));
            }
            return new ResponseEntity<ArrayList<UserDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<UserDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/real-estates/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<ArrayList<RealEstateDTO>> getRealEstatesForGroup(@PathVariable Integer groupId){

        try {
            List<RealEstate> realEstates = groupService.getRealEstatesForGroup(groupId);
            ArrayList<RealEstateDTO> dtos = new ArrayList<>();
            for (RealEstate r: realEstates) {
                dtos.add(new RealEstateDTO(r));
            }
            return new ResponseEntity<ArrayList<RealEstateDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<RealEstateDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_GROUP')")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody String name){

        try {
            Group group = groupService.createGroup(name);
            return new ResponseEntity<GroupDTO>(new GroupDTO(group), HttpStatus.OK);
        } catch (NotUniqueException e) {
            e.printStackTrace();
            return new ResponseEntity<GroupDTO>(new GroupDTO(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/outsiders/{groupId}")
    @PreAuthorize("hasAuthority('READ_ONE_GROUP')")
    public ResponseEntity<ArrayList<UserDTO>> getOutsidersForGroup(@PathVariable Integer groupId){

        try {
            List<User> users = groupService.getOutsiders(groupId);
            ArrayList<UserDTO> dtos = new ArrayList<>();
            for (User u: users) {
                dtos.add(new UserDTO(u));
            }
            return new ResponseEntity<ArrayList<UserDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<UserDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
