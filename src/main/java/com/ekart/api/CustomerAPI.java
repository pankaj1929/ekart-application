package com.ekart.api;

import com.ekart.dto.CustomerDTO;
import com.ekart.exception.EKartException;
import com.ekart.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Validated
//Add the missing annotations
@CrossOrigin
@RequestMapping(value = "/customer-api")

public class CustomerAPI {
    static Log logger = LogFactory.getLog(CustomerAPI.class);
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestTemplate template;
    @Autowired
    private Environment environment;

    @PostMapping(value = "/login")
    public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerDTO customerDTO)
            throws EKartException {

        logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
        CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(customerDTO.getEmailId(),
                customerDTO.getPassword());
        logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerDTOFromDB.getEmailId());
        return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws EKartException {

        logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
        String registeredWithEmailID = customerService.registerNewCustomer(customerDTO);
        registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")
                + registeredWithEmailID;
        return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/{customerEmailId:.+}/address/")
    public ResponseEntity<String> updateShippingAddress(
            @PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
            @RequestBody String address) throws EKartException {

        customerService.updateShippingAddress(customerEmailId, address);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }

    @DeleteMapping(value = "/customer/{customerEmailId:.+}")
    public ResponseEntity<String> deleteShippingAddress(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws EKartException {

        customerService.deleteShippingAddress(customerEmailId);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }

}