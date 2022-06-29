package antihackerman.controller;

import antihackerman.dto.RealEstateDTO;
import antihackerman.exceptions.NotFoundException;
import antihackerman.logs.LogType;
import antihackerman.messaging.Message;
import antihackerman.model.RealEstate;
import antihackerman.service.DeviceService;
import antihackerman.service.LogService;
import antihackerman.service.RealEstateService;
import antihackerman.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3010/" })
@RestController
@RequestMapping("/real-estates")
public class RealEstateController {
    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;


    @GetMapping("/{groupId}/{username}")
    @PreAuthorize("hasAuthority('READ_REALESTATES_USER')")
    public ResponseEntity<ArrayList<RealEstateDTO>> getRealEstatesForUserAndGroup(@PathVariable Integer groupId, @PathVariable String username, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        try {
            Set<RealEstate> realEstates = realEstateService.getAllForUserAndGroup(groupId, username);
            ArrayList<RealEstateDTO> dtos = new ArrayList<>();
            for (RealEstate r: realEstates) {
                dtos.add(new RealEstateDTO(r));
            }
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected their real estates in group.");

            return new ResponseEntity<ArrayList<RealEstateDTO>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested their real estates in group with entities not in database.");

            return new ResponseEntity<ArrayList<RealEstateDTO>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_REALESTATE')")
    public ResponseEntity<RealEstateDTO> getOne(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        try {
            RealEstate realEstate = realEstateService.getById(id);
            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected real estate.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(realEstate), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested real estate not in database.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.NOT_FOUND);
        }

    }
}
