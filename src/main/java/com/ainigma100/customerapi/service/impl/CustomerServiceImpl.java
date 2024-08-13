package com.ainigma100.customerapi.service.impl;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.entity.Customer;
import com.ainigma100.customerapi.exception.ResourceAlreadyExistException;
import com.ainigma100.customerapi.exception.ResourceNotFoundException;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.repository.CustomerRepository;
import com.ainigma100.customerapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Customer recordFromDB = customerRepository.findByEmail(customerDTO.getEmail());

        if (recordFromDB != null) {
            throw new ResourceAlreadyExistException("Customer", "email", customerDTO.getEmail());
        }

        Customer recordToBeSaved = customerMapper.customerDTOToCustomer(customerDTO);

        Customer savedRecord = customerRepository.save(recordToBeSaved);

        return customerMapper.customerToCustomerDTO(savedRecord);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        return customerMapper.customerToCustomerDTO(recordFromDB);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        // just to be safe that the object does not have another id
        customerDTO.setId(recordFromDB.getId());

        Customer recordToBeSaved = customerMapper.customerDTOToCustomer(customerDTO);

        Customer savedRecord = customerRepository.save(recordToBeSaved);

        return customerMapper.customerToCustomerDTO(savedRecord);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        customerRepository.delete(recordFromDB);
    }
}
