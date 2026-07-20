package com.ainigma100.customerapi.controller;


import com.ainigma100.customerapi.dto.*;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Operation(summary = "Add a new customer")
    @PostMapping
    public ResponseEntity<APIResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO,
            UriComponentsBuilder uriComponentsBuilder) {

        CustomerDTO customerDTO = customerMapper.customerRequestDTOToCustomerDTO(customerRequestDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> response = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        URI location = uriComponentsBuilder
                .path("/api/v1/customers/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
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

    @Operation(summary = "Partially update a customer's email")
    @PatchMapping("/{id}/email")
    public ResponseEntity<APIResponse<CustomerDTO>> updateCustomerEmail(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerEmailUpdateDTO emailUpdateDTO) {

        CustomerDTO result = customerService.updateCustomerEmail(id, emailUpdateDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> response = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Delete a customer by ID")
    @PreAuthorize("hasRole('ADMIN')")
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


    @Operation(summary = "Search customers with pagination",
            description = "Returns a paginated list of customers based on the search criteria")
    @PostMapping("/search")
    public ResponseEntity<APIResponse<Page<CustomerDTO>>> getAllCustomersUsingPagination(
            @Valid @RequestBody CustomerSearchCriteriaDTO customerSearchCriteriaDTO) {

        Page<CustomerDTO> result = customerService.getAllCustomersUsingPagination(customerSearchCriteriaDTO);

        // Builder Design pattern
        APIResponse<Page<CustomerDTO>> responseDTO = APIResponse
                .<Page<CustomerDTO>>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
