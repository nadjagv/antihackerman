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
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/real-estates")
public class RealEstateController {
    @Autowired
    private RealEstateService realEstateService;

    @PostMapping()
    public ResponseEntity<RealEstateDTO> createRealEstate(@RequestBody RealEstateDTO dto){

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
}
