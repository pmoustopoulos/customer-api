package com.ainigma100.customerapi.controller;


import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerRequestDTO;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Operation(summary = "Add a new customer")
    @PostMapping
    public ResponseEntity<APIResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerDTO customerDTO = customerMapper.customerRequestDTOToCustomerDTO(customerRequestDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> response = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Find customer by ID",
            description = "Returns a single customer")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CustomerDTO>> getCustomerById(@PathVariable("id") Long id) {

        CustomerDTO result = customerService.getCustomerById(id);

        // Builder Design pattern
        APIResponse<CustomerDTO> responseDTO = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


    @Operation(summary = "Update an existing customer")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CustomerDTO>> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerDTO customerDTO = customerMapper.customerRequestDTOToCustomerDTO(customerRequestDTO);

        CustomerDTO result = customerService.updateCustomer(id, customerDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> responseDTO = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


    @Operation(summary = "Delete a customer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCustomer(@PathVariable("id") Long id) {

        customerService.deleteCustomer(id);

        String result = "Customer deleted successfully";

        // Builder Design pattern
        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


}
