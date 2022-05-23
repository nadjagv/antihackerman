package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.dto.RegistrationDTO;
import antihackerman.backendapp.dto.UserDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<ArrayList<UserDTO>> getAll(){

        try {
            ArrayList<User> users = (ArrayList<User>) userService.getAll();
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

    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getById(@PathVariable Integer id){

        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){

        try {
            User user = userService.getUserByUsername(username);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email){

        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        try {
            userService.deleteById(id);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("User not in database.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/fromGroup/{id}/{groupId}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @PathVariable Integer groupId){
        try {
            userService.deleteFromGroupById(id, groupId);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("User not in database.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('REGISTER_USER')")
    public ResponseEntity<UserDTO> register(@RequestBody RegistrationDTO dto){
        if (dto.getGroupId() == null || dto.getRoles()==null || dto.getEmail() == null
                || dto.getUsername()==null || dto.getPassword() == null){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (dto.getRoles().isEmpty()){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (!dto.getRoles().contains("ROLE_OWNER")&&dto.getRoles().contains("ROLE_TENANT")){
            if (dto.getRealestate_ids() == null){
                return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
            }else if (dto.getRealestate_ids().isEmpty()){
                return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        try {
            User user = userService.registerUser(dto);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        } catch (NotUniqueException e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.CONFLICT);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/to-owner/{userId}/{groupId}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<UserDTO> changeToOwner(@PathVariable Integer userId, @PathVariable Integer groupId,
                                                 @RequestBody List<Integer> realestateIds){
        if (realestateIds == null){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realestateIds.isEmpty()){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            User user = userService.changeRole(groupId, userId, "ROLE_OWNER", realestateIds);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/to-tenant/{userId}/{groupId}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<UserDTO> changeToTenant(@PathVariable Integer userId, @PathVariable Integer groupId,
                                                  @RequestBody List<Integer> realestateIds){
        if (realestateIds == null){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realestateIds.isEmpty()){
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            User user = userService.changeRole(groupId, userId, "ROLE_TENANT", realestateIds);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/groupsOwning/{userId}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<ArrayList<GroupDTO>> getGroupsOwning(@PathVariable Integer userId){

        try {
            ArrayList<Group> groups = (ArrayList<Group>) userService.getGroupsOwning(userId);
            ArrayList<GroupDTO> dtos = new ArrayList<>();
            for (Group g: groups) {
                dtos.add(new GroupDTO(g));
            }
            return new ResponseEntity<ArrayList<GroupDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<GroupDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/realestatesTenanting/{userId}")
    @PreAuthorize("hasAuthority('READ_ONE_USER')")
    public ResponseEntity<ArrayList<RealEstateDTO>> getRealestatesTenanting(@PathVariable Integer userId){

        try {
            List<RealEstate> groups = userService.getRealestatesTenanting(userId);
            ArrayList<RealEstateDTO> dtos = new ArrayList<>();
            for (RealEstate g: groups) {
                dtos.add(new RealEstateDTO(g));
            }
            return new ResponseEntity<ArrayList<RealEstateDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<RealEstateDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
}
