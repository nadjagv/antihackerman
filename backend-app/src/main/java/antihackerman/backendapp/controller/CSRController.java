package antihackerman.backendapp.controller;


import antihackerman.backendapp.dto.CSRdto;
import antihackerman.backendapp.dto.ExtensionDTO;
import antihackerman.backendapp.model.CSR;
import antihackerman.backendapp.model.SubjectData;
import antihackerman.backendapp.service.CSRService;
import antihackerman.backendapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.security.cert.Certificate;
import java.util.ArrayList;

@RestController
@RequestMapping("/csr")
public class CSRController {

    @Autowired
    CSRService csrService;

    @PostMapping(path = "/uploadCSR", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_CSR')")
    public ResponseEntity uploadCSR(@RequestParam(("file")) MultipartFile q) throws Exception {
        String csr = FileUtil.readFile(q);
        csrService.addRequest(csr);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/generateCSR")
    @PreAuthorize("hasAuthority('CREATE_CSR')")
    public ResponseEntity generateCSR(@RequestBody CSRdto dto) throws Exception {
        SubjectData subjectData = csrService.generateSubjectDataCSR(dto);
        csrService.generateCSR(subjectData);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('PKI_ACCESS')")
    public ResponseEntity<ArrayList<CSRdto>> getAll(){

        try {
            ArrayList<CSR> csrs = (ArrayList<CSR>) csrService.readAllCSRs();
            ArrayList<CSRdto> dtos = new ArrayList<>();
            for (CSR c: csrs) {
                dtos.add(new CSRdto(c));
            }
            return new ResponseEntity<ArrayList<CSRdto>>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ArrayList<CSRdto>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{uniqueFilename}")
    @PreAuthorize("hasAuthority('PKI_ACCESS')")
    public ResponseEntity<CSRdto> getOne(@PathVariable String uniqueFilename){

        try {
            CSR csr = csrService.readSingleCSR(uniqueFilename);
            return new ResponseEntity<CSRdto>(new CSRdto(csr), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<CSRdto>(new CSRdto(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rejectCSR/{uniqueFilename}")
    @PreAuthorize("hasAuthority('PKI_ACCESS')")
    public ResponseEntity<String> rejectCSR(@PathVariable String uniqueFilename){

        try {
            csrService.rejectCSR(uniqueFilename);
            return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("file does not exist",HttpStatus.NOT_FOUND);
        }


    }

    @GetMapping("/approveCSR/{uniqueFilename}")
    @PreAuthorize("hasAuthority('PKI_ACCESS')")
    public ResponseEntity<String> approveCSR(@PathVariable String uniqueFilename){

        try {
            csrService.approveCSR(uniqueFilename, new ArrayList<ExtensionDTO>());
            return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("file does not exist",HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/approveCSRextensions/{uniqueFilename}")
    @PreAuthorize("hasAuthority('PKI_ACCESS')")
    public ResponseEntity<String> approveCSRExtensions(@PathVariable String uniqueFilename, @RequestBody ArrayList<ExtensionDTO> exceptiodDTOs){

        try {
            csrService.approveCSR(uniqueFilename, exceptiodDTOs);
            return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("file does not exist",HttpStatus.NOT_FOUND);
        }


    }
}
