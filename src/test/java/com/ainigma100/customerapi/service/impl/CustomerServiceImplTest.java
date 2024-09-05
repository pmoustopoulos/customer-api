package com.ainigma100.customerapi.service.impl;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerEmailUpdateDTO;
import com.ainigma100.customerapi.dto.CustomerSearchCriteriaDTO;
import com.ainigma100.customerapi.entity.Customer;
import com.ainigma100.customerapi.exception.ResourceAlreadyExistException;
import com.ainigma100.customerapi.exception.ResourceNotFoundException;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
 * @ExtendWith(MockitoExtension.class) informs Mockito that we are using
 * mockito annotations to mock the dependencies
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    // @InjectMocks creates the mock object of the class and injects the mocks
    // that are marked with the annotations @Mock into it.
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerDTO customerDTO;
    private CustomerSearchCriteriaDTO customerSearchCriteriaDTO;

    /**
     * This method will be executed before each and every test inside this class
     */
    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));
        customer.setCreatedDate(LocalDateTime.now());
        customer.setUpdatedDate(LocalDateTime.now());

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Wick");
        customerDTO.setEmail("jwick@tester.com");
        customerDTO.setPhoneNumber("0123456789");
        customerDTO.setDateOfBirth(LocalDate.now().minusYears(18));

        customerSearchCriteriaDTO = new CustomerSearchCriteriaDTO();
        customerSearchCriteriaDTO.setPage(0);
        customerSearchCriteriaDTO.setSize(10);

    }


    @Test
    @DisplayName("Test creating a new customer")
    void givenCustomerDTO_whenCreateCustomer_thenReturnCustomerDTO() {

        // given - precondition or setup
        String email = customerDTO.getEmail();
        given(customerRepository.findByEmail(email)).willReturn(Optional.empty());
        given(customerMapper.customerDTOToCustomer(customerDTO)).willReturn(customer);
        given(customerRepository.save(customer)).willReturn(customer);
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerMapper, times(1)).customerDTOToCustomer(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }

    @Test
    @DisplayName("Test creating a customer with existing email throws ResourceAlreadyExistException")
    void givenExistingEmail_whenCreateCustomer_thenThrowResourceAlreadyExistException() {

        // given - precondition or setup
        String email = customerDTO.getEmail();
        given(customerRepository.findByEmail(email)).willReturn(Optional.of(customer));

        // when/then - verify that the ResourceAlreadyExistException is thrown
        assertThatThrownBy(() -> customerService.createCustomer(customerDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Resource Customer with email : '" + email + "' already exist");


        verify(customerRepository, times(1)).findByEmail(customerDTO.getEmail());
        verify(customerMapper, never()).customerDTOToCustomer(any(CustomerDTO.class));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }

    @Test
    @DisplayName("Test retrieving a customer by ID")
    void givenValidId_whenGetCustomerById_thenReturnCustomerDTO() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.getCustomerById(id);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(customerDTO.getId());
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }


    @Test
    @DisplayName("Test retrieving a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidId_whenGetCustomerById_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 100L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");


        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }


    @Test
    @DisplayName("Test updating a customer by ID")
    void givenValidIdAndCustomerDTO_whenUpdateCustomer_thenReturnUpdatedCustomerDTO() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerMapper.customerDTOToCustomer(customerDTO)).willReturn(customer);
        given(customerRepository.save(customer)).willReturn(customer);
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.updateCustomer(id, customerDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, times(1)).customerDTOToCustomer(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }

    @Test
    @DisplayName("Test updating a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidIdAndCustomerDTO_whenUpdateCustomer_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 100L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.updateCustomer(id, customerDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");


        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).customerDTOToCustomer(any(CustomerDTO.class));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }


    @Test
    @DisplayName("Test updating a customer's email by ID")
    void givenValidIdAndCustomerEmailUpdateDTO_whenUpdateCustomerEmail_thenReturnCustomerDTO() {

        // given - precondition or setup
        Long id = 1L;
        CustomerEmailUpdateDTO customerEmailUpdateDTO = new CustomerEmailUpdateDTO();
        customerEmailUpdateDTO.setEmail("loco@gmail.com");
        customer.setEmail(customerEmailUpdateDTO.getEmail());
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerRepository.save(customer)).willReturn(customer);
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.updateCustomerEmail(id, customerEmailUpdateDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }

    @Test
    @DisplayName("Test updating a customer's email by invalid ID throws ResourceNotFoundException")
    void givenInvalidIdAndCustomerEmailUpdateDTO_whenUpdateCustomerEmail_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 100L;
        CustomerEmailUpdateDTO customerEmailUpdateDTO = new CustomerEmailUpdateDTO();
        customerEmailUpdateDTO.setEmail("loco@gmail.com");
        customer.setEmail(customerEmailUpdateDTO.getEmail());

        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.updateCustomerEmail(id, customerEmailUpdateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");


        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }


    @Test
    @DisplayName("Test deleting a customer by ID")
    void givenValidId_whenDeleteCustomer_thenDeleteCustomer() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        // when - action or behaviour that we are going to test
        customerService.deleteCustomer(id);

        // then - verify the output
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).delete(customer);

    }

    @Test
    @DisplayName("Test deleting a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidId_whenDeleteCustomer_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).delete(any(Customer.class));

    }


    @Test
    void givenCustomerSearchCriteriaDTO_whenGetAllCustomersUsingPagination_thenReturnCustomerDTOPage() {

        // given - precondition or setup
        List<Customer> customerList = Collections.singletonList(customer);
        Page<Customer> customerPage = new PageImpl<>(customerList);
        given(customerRepository.getAllCustomersUsingPagination(eq(customerSearchCriteriaDTO), any(Pageable.class)))
                .willReturn(customerPage);

        given(customerMapper.customerListToCustomerDTOList(customerPage.getContent()))
                .willReturn(Collections.singletonList(customerDTO));

        // when - action or behaviour that we are going to test
        Page<CustomerDTO> result = customerService.getAllCustomersUsingPagination(customerSearchCriteriaDTO);

        // then - verify the output
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getContent().get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getContent().get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getContent().get(0).getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(result.getContent().get(0).getDateOfBirth()).isEqualTo(customer.getDateOfBirth());

        verify(customerRepository, times(1)).getAllCustomersUsingPagination(eq(customerSearchCriteriaDTO), any(Pageable.class));
        verify(customerMapper, times(1)).customerListToCustomerDTOList(customerPage.getContent());
    }


}
