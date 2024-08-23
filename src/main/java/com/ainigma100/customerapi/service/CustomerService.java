package com.ainigma100.customerapi.service;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerEmailUpdateDTO;
import com.ainigma100.customerapi.dto.CustomerSearchCriteriaDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerById(Long id);

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    CustomerDTO updateCustomerEmail(Long id, CustomerEmailUpdateDTO emailUpdateDTO);

    void deleteCustomer(Long id);

    Page<CustomerDTO> getAllCustomersUsingPagination(CustomerSearchCriteriaDTO customerSearchCriteriaDTO);

}
