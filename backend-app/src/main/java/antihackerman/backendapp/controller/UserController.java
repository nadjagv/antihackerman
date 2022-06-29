package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.dto.RegistrationDTO;
import antihackerman.backendapp.dto.UserDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.LogService;
import antihackerman.backendapp.service.UserService;
import antihackerman.backendapp.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<ArrayList<UserDTO>> getAll(@RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            List<User> users =  userService.getAll();
            ArrayList<UserDTO> dtos = new ArrayList<>();
            for (User u: users) {
                dtos.add(new UserDTO(u));
            }
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected all users.");

            return new ResponseEntity<ArrayList<UserDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested all users, but no found.");

            return new ResponseEntity<ArrayList<UserDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getById(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            User user = userService.getUserById(id);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected user by id.");

            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested user by id not in database.");
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        try {
            User user = userService.getUserByUsername(username);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected user by username.");
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested user by username not in database.");
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            User user = userService.getUserByEmail(email);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected user by email.");
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested user by email not in database.");
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            userService.deleteById(id);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" deleted user by id.");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested delete user by id not in database.");
            return new ResponseEntity<String>("User not in database.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/fromGroup/{id}/{groupId}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @PathVariable Integer groupId, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            userService.deleteFromGroupById(id, groupId);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" deleted user from group by id.");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" requested delete user from group by id not in database.");
            return new ResponseEntity<String>("User not in database.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('REGISTER_USER')")
    public ResponseEntity<UserDTO> register(@RequestBody RegistrationDTO dto, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        if (dto.getGroupId() == null || dto.getRoles()==null || dto.getEmail() == null
                || dto.getUsername()==null || dto.getPassword() == null){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user missing data.");
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (dto.getRoles().isEmpty()){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user missing data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (!dto.getRoles().contains("ROLE_OWNER")&&dto.getRoles().contains("ROLE_TENANT")){
            if (dto.getRealestate_ids() == null){
                logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user missing data.");

                return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
            }else if (dto.getRealestate_ids().isEmpty()){
                logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user missing data.");

                return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        try {
            User user = userService.registerUser(dto);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" registered user.");

            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user invalid data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to register user with entities not in database.");
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        } catch (NotUniqueException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to register user but not unique username or email.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.CONFLICT);
        } catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to register user but exception occured.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/to-owner/{userId}/{groupId}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<UserDTO> changeToOwner(@PathVariable Integer userId, @PathVariable Integer groupId,
                                                 @RequestBody List<Integer> realestateIds, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        if (realestateIds == null){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user missing data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realestateIds.isEmpty()){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user missing data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            User user = userService.changeRole(groupId, userId, "ROLE_OWNER", realestateIds);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" edited user to owner.");

            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }  catch (NotFoundException e) {
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user but with entities not in database.");

            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user but exception occurred.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/to-tenant/{userId}/{groupId}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<UserDTO> changeToTenant(@PathVariable Integer userId, @PathVariable Integer groupId,
                                                  @RequestBody List<Integer> realestateIds, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        if (realestateIds == null){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user missing data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realestateIds.isEmpty()){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user missing data.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            User user = userService.changeRole(groupId, userId, "ROLE_TENANT", realestateIds);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" edited user to tenant.");

            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user but with entities not in database.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent request to edit user but exception occurred.");

            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/groupsOwning/{userId}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<ArrayList<GroupDTO>> getGroupsOwning(@PathVariable Integer userId, @RequestHeader (name="Authorization") String token, HttpServletRequest request){


        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            Set<Group> groups = userService.getGroupsOwning(userId);
            ArrayList<GroupDTO> dtos = new ArrayList<>();
            for (Group g: groups) {
                dtos.add(new GroupDTO(g));
            }
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected groups owning for user.");

            return new ResponseEntity<ArrayList<GroupDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested groups owning for user not in databse.");
            return new ResponseEntity<ArrayList<GroupDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/realestatesTenanting/{userId}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<ArrayList<RealEstateDTO>> getRealestatesTenanting(@PathVariable Integer userId, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            Set<RealEstate> groups = userService.getRealestatesTenanting(userId);
            ArrayList<RealEstateDTO> dtos = new ArrayList<>();
            for (RealEstate g: groups) {
                dtos.add(new RealEstateDTO(g));
            }
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected real estates tenanting for user.");
            return new ResponseEntity<ArrayList<RealEstateDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested real estates tenanting for user not in database.");

            return new ResponseEntity<ArrayList<RealEstateDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
