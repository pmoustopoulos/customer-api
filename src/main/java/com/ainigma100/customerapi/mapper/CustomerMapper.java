package com.ainigma100.customerapi.mapper;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerDTO customerDTO);

    CustomerDTO toCustomerDTO(Customer customer);

    List<Customer> toCustomerList(List<CustomerDTO> customerDTOList);

    List<CustomerDTO> toCustomerDTOList(List<Customer> customerList);

}
