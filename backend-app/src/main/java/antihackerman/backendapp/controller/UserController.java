package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.GroupDTO;
import antihackerman.backendapp.dto.RegistrationDTO;
import antihackerman.backendapp.dto.UserDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
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
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        try {
            userService.deleteById(id);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("User not in database.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<UserDTO> register(@RequestBody RegistrationDTO dto){

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
}
