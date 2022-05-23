package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.dto.UserDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/real-estates")
public class RealEstateController {
    @Autowired
    private RealEstateService realEstateService;

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_REALESTATE')")
    public ResponseEntity<RealEstateDTO> createRealEstate(@RequestBody RealEstateDTO dto){
        if (dto.getGroupId() == null || dto.getLocation() == null || dto.getName() == null || dto.getLocation().equals("") || dto.getName().equals("")){
            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            RealEstate realEstate = realEstateService.createRealestate(dto);
            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(realEstate), HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/add-tenant/{userId}")
    @PreAuthorize("hasAuthority('EDIT_REALESTATE')")
    public ResponseEntity<String> addTenant(@PathVariable Integer userId, @RequestBody List<Integer> realEstateIds){
        if (realEstateIds == null){
            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realEstateIds.isEmpty()){
            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            realEstateService.editRealEstates(userId, realEstateIds, true);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Exception", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("Exception", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/remove-tenant/{userId}")
    @PreAuthorize("hasAuthority('EDIT_REALESTATE')")
    public ResponseEntity<String> removeTenant(@PathVariable Integer userId, @RequestBody List<Integer> realEstateIds){
        if (realEstateIds == null){
            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realEstateIds.isEmpty()){
            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            realEstateService.editRealEstates(userId, realEstateIds, false);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Exception", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("Exception", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_REALESTATE')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        try {
            realEstateService.deleteById(id);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Real estate not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
