package antihackerman.backendapp.controller;

import java.math.BigInteger;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antihackerman.backendapp.dto.CertificateDTO;
import antihackerman.backendapp.service.CRLService;
import antihackerman.backendapp.service.CertificateService;

import java.security.cert.CRLReason;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/cert")
public class CertificateController {
	
	@Autowired
	private CertificateService certService;
	
	@Autowired
	private CRLService crlService;
	
	@GetMapping("")
	public ResponseEntity<List<CertificateDTO>> getAllCertificates(){

		List<CertificateDTO> dtos=certService.getAllCerts();
		return new ResponseEntity<List<CertificateDTO>>(dtos,HttpStatus.OK);

	}
	
	@GetMapping("/{alias}")
	public ResponseEntity<String> getCertificate(@PathVariable String alias){

		Certificate cert=certService.getCertificateByAlias(alias);
		return new ResponseEntity<String>(cert.toString(),HttpStatus.OK);

	}
	
	@GetMapping("/validity/{alias}")
	public ResponseEntity<Boolean> checkValidity(@PathVariable String alias){

		boolean check=certService.checkValidityForCertificate(alias);
		return new ResponseEntity<Boolean>(check,HttpStatus.OK);

	}
	
	@PutMapping("/crl/reset")
	public void createCRL(){
		crlService.createCRL();
	}
	
	@PostMapping("/crl/{serial}/{reason}")
	public void revokeCert(@PathVariable Integer serial,@PathVariable String reason){
		System.out.println(reason);
		crlService.revokeCert(new BigInteger(serial.toString()),CRLReason.valueOf(reason));
	}
	
	@GetMapping("/crl/{serial}")
	public ResponseEntity<Boolean> checkCertRevocation(@PathVariable Integer serial){
		boolean check=crlService.checkRevocation(new BigInteger(serial.toString()));
		return new ResponseEntity<Boolean>(check,HttpStatus.OK);
	}

}
