package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.RegistrationDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.Role;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.repository.GroupRepository;
import antihackerman.backendapp.repository.RealEstateRepository;
import antihackerman.backendapp.repository.RoleRepository;
import antihackerman.backendapp.repository.UserRepository;
import antihackerman.backendapp.util.InputValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRep;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAll (){
        return userRep.findAll();
    }

    public User getUserById(Integer id) throws NotFoundException {
        User result = userRep.getById(id);
        if(result==null){
            throw new NotFoundException("User with id "+id+" does not exist.");
        }
        return result;
    }

    public User getUserByUsername(String username) throws NotFoundException {
        User result = userRep.findOneByUsername(username);
        if(result==null){
            throw new NotFoundException("User with username "+username+" does not exist.");
        }
        return result;
    }

    public User getUserByEmail(String email) throws NotFoundException {
        User result = userRep.findOneByEmail(email);
        if(result==null){
            throw new NotFoundException("User with email "+email+" does not exist.");
        }
        return result;
    }

    public void deleteByUsername(String username) throws NotFoundException {
        User found = userRep.findOneByUsername(username);
        if(found==null){
            throw new NotFoundException("User with username "+username+" does not exist.");
        }
        userRep.delete(found);
    }

    public void deleteById(Integer id) throws NotFoundException {
        User found = userRep.getById(id);
        if(found==null){
            throw new NotFoundException("User with id "+id+" does not exist.");
        }
        userRep.delete(found);
    }

    public User registerUser(RegistrationDTO registrationDTO) throws InvalidInputException, NotFoundException, NotUniqueException {

        if (!InputValidationUtil.isEmailValid(registrationDTO.getEmail())){
            throw new InvalidInputException("Invalid email.");
        }else if (userRep.findOneByEmail(registrationDTO.getEmail()) != null){
            throw new NotUniqueException("Email already in use.");
        }

        if(!InputValidationUtil.isPasswordValid(registrationDTO.getPassword())){
            throw new InvalidInputException("Invalid password.");
        }

        if(!InputValidationUtil.isUsernameValid(registrationDTO.getUsername())){
            throw new InvalidInputException("Invalid username.");
        }else if (userRep.findOneByUsername(registrationDTO.getUsername()) != null){
            throw new NotUniqueException("Username already in use.");
        }

        List<Group> groups_owning = new ArrayList<Group>();
        List<RealEstate> realestates_tenanting = new ArrayList<RealEstate>();
        List<Role> roles = new ArrayList<Role>();

        if (registrationDTO.getRoles().contains("ROLE_OWNER")){
            Group group = groupRepository.getById(registrationDTO.getGroupId());
            if(group==null){
                throw new NotFoundException("Group with id "+registrationDTO.getGroupId()+" does not exist.");
            }
            groups_owning.add(group);
        }

        if (registrationDTO.getRoles().contains("ROLE_TENANT")){
            List<Integer> realestate_ids = registrationDTO.getRealestate_ids().stream().distinct().collect(Collectors.toList());
            for (Integer realEstateId: realestate_ids) {
                RealEstate realEstate = realEstateRepository.getById(realEstateId);
                if(realEstate==null){
                    throw new NotFoundException("Real estate with id "+realEstateId+" does not exist.");
                }
                realestates_tenanting.add(realEstate);
            }
        }

        List<String> roles_dto = registrationDTO.getRoles().stream().distinct().collect(Collectors.toList());

        for (String role_name: roles_dto) {
            Role role = roleRepository.findOneByRole(role_name);
            if(role==null){
                throw new NotFoundException("Role with name "+role_name+" does not exist.");
            }
            roles.add(role);
        }

        User newUser = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(registrationDTO.getPassword())
                .groups_owning(groups_owning)
                .realestates_tenanting(realestates_tenanting)
                .roles(roles)
                .deleted(false)
                .lastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return userRep.save(newUser);
    }

}
