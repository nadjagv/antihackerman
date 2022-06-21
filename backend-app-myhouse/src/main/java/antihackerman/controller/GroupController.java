package antihackerman.controller;

import antihackerman.dto.GroupDTO;
import antihackerman.model.Group;
import antihackerman.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('READ_GROUPS_USER')")
    public ResponseEntity<ArrayList<GroupDTO>> getForUser(@PathVariable Integer userId){

        try {
            Set<Group> groups = groupService.getAllForUser(userId);
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
}
