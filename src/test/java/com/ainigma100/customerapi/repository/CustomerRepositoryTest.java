package com.ainigma100.customerapi.repository;

import com.ainigma100.customerapi.dto.CustomerSearchCriteriaDTO;
import com.ainigma100.customerapi.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/*
 * @DataJpaTest will automatically configure in-memory database for testing
 * and, it will not load annotated beans into the Application Context.
 * It will only load the repository class. Tests annotated with @DataJpaTest
 * are by default transactional and roll back at the end of each test.
 */
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    /**
     * This method will be executed before each and every test inside this class
     */
    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnCustomer() {

        // given - precondition or setup
        String email = "jwick@tester.com";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email).orElse(null);

        // then - verify the output
        assertNotNull(customerFromDB);
        assertEquals(customer.getFirstName(), customerFromDB.getFirstName());
        assertEquals(customer.getLastName(), customerFromDB.getLastName());
        assertEquals(customer.getEmail(), customerFromDB.getEmail());
        assertEquals(customer.getPhoneNumber(), customerFromDB.getPhoneNumber());
        assertEquals(customer.getDateOfBirth(), customerFromDB.getDateOfBirth());
    }

    @Test
    void givenInvalidEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = "abc@tester.com";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email).orElse(null);

        // then - verify the output
        assertNull(customerFromDB);
    }

    @Test
    void givenNullEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = null;
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email).orElse(null);

        // then - verify the output
        assertNull(customerFromDB);
    }

    @Test
    void givenEmptyEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = "";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email).orElse(null);

        // then - verify the output
        assertNull(customerFromDB);
    }

    @Test
    void givenCustomerSearchCriteriaDTO_whenGetAllCustomersUsingPagination_thenReturnCustomerDTOPage() {

        // given - precondition or setup
        customerRepository.save(customer);

        CustomerSearchCriteriaDTO searchCriteriaDTO = new CustomerSearchCriteriaDTO();
        searchCriteriaDTO.setFirstName("John");

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when - action or behaviour that we are going to test
        Page<Customer> customerPage = customerRepository.getAllCustomersUsingPagination(searchCriteriaDTO, pageRequest);

        // then - verify the output
        assertThat(customerPage).isNotNull();
        assertThat(customerPage.getContent()).hasSize(1);
        assertThat(customerPage.getContent().get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(customerPage.getContent().get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(customerPage.getContent().get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerPage.getContent().get(0).getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(customerPage.getContent().get(0).getDateOfBirth()).isEqualTo(customer.getDateOfBirth());
    }

    @Test
    void givenInvalidCustomerSearchCriteriaDTO_whenGetAllCustomersUsingPagination_thenReturnEmptyPage() {

        // given - precondition or setup
        customerRepository.save(customer);

        CustomerSearchCriteriaDTO searchCriteriaDTO = new CustomerSearchCriteriaDTO();
        searchCriteriaDTO.setFirstName("xxxxx");

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when - action or behaviour that we are going to test
        Page<Customer> customerPage = customerRepository.getAllCustomersUsingPagination(searchCriteriaDTO, pageRequest);

        // then - verify the output
        assertThat(customerPage.getContent()).isEmpty();

    }

}
