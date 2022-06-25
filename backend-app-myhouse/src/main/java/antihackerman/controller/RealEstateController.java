package antihackerman.controller;

import antihackerman.dto.RealEstateDTO;
import antihackerman.model.RealEstate;
import antihackerman.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

//@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/real-estates")
public class RealEstateController {
    @Autowired
    private RealEstateService realEstateService;

    @GetMapping("/{groupId}/{username}")
    @PreAuthorize("hasAuthority('READ_REALESTATES_USER')")
    public ResponseEntity<ArrayList<RealEstateDTO>> getRealEstatesForUserAndGroup(@PathVariable Integer groupId, @PathVariable String username){

        try {
            Set<RealEstate> realEstates = realEstateService.getAllForUserAndGroup(groupId, username);
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
}
