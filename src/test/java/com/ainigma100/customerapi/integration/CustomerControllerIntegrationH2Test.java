package com.ainigma100.customerapi.integration;

import com.ainigma100.customerapi.dto.CustomerEmailUpdateDTO;
import com.ainigma100.customerapi.dto.CustomerRequestDTO;
import com.ainigma100.customerapi.dto.CustomerSearchCriteriaDTO;
import com.ainigma100.customerapi.entity.Customer;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.repository.CustomerRepository;
import com.ainigma100.customerapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests using in-memory H2 database.
 * Note: Normally during integration tests we do not use H2. We use a real database (via Testcontainers)
 * identical to production. This H2-based test exists only so people without Docker can still run
 * the integration tests locally. For the real setup, see CustomerControllerIntegrationTest.
 * Uses the `test` Spring profile which is configured in src/test/resources/application-test.yaml
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIntegrationH2Test {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // clean the database before we start each test
        customerRepository.deleteAll();
    }

    @Test
    void givenCustomerDTO_whenCreateCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("John");
        customerRequestDTO.setLastName("Wick");
        customerRequestDTO.setEmail("jwick@tester.com");
        customerRequestDTO.setPhoneNumber("0123456789");
        customerRequestDTO.setDateOfBirth(LocalDate.now().minusYears(18));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/customers")
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(customerRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customerRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customerRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is("*******789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(customerRequestDTO.getDateOfBirth().toString())));
    }

    @Test
    void givenCustomerDTO_whenGetCustomerById_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", customer.getId())
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", equalTo(Math.toIntExact(customer.getId()))))
                .andExpect(jsonPath("$.results.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customer.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customer.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is("*******789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(customer.getDateOfBirth().toString())));
    }

    @Test
    void givenCustomerDTO_whenUpdateCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer);

        CustomerRequestDTO customerUpdateRequestDTO = new CustomerRequestDTO();
        customerUpdateRequestDTO.setFirstName("Mark");
        customerUpdateRequestDTO.setLastName("Kent");
        customerUpdateRequestDTO.setEmail("mkent@tester.com");
        customerUpdateRequestDTO.setPhoneNumber("0123456700");
        customerUpdateRequestDTO.setDateOfBirth(LocalDate.now().minusYears(18));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", customer.getId())
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateRequestDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", equalTo(Math.toIntExact(customer.getId()))))
                .andExpect(jsonPath("$.results.firstName", is(customerUpdateRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customerUpdateRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customerUpdateRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is("*******700")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(customerUpdateRequestDTO.getDateOfBirth().toString())));
    }

    @Test
    void givenCustomerEmailUpdateDTO_whenUpdateCustomerEmail_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer);

        CustomerEmailUpdateDTO customerEmailUpdateDTO = new CustomerEmailUpdateDTO();
        customerEmailUpdateDTO.setEmail("test@test.com");

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(patch("/api/v1/customers/{id}/email", customer.getId())
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerEmailUpdateDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", equalTo(Math.toIntExact(customer.getId()))))
                .andExpect(jsonPath("$.results.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customer.getLastName())))
                .andExpect(jsonPath("$.results.email", is("test@test.com")))
                .andExpect(jsonPath("$.results.phoneNumber", is("*******789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(customer.getDateOfBirth().toString())));
    }

    @Test
    void givenCustomerDTO_whenDeleteCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/customers/{id}", customer.getId())
                .header("Authorization", "Bearer admin-token")
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));
    }

    @Test
    void givenUserRole_whenDeleteCustomer_thenForbidden() throws Exception {
        // given
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));
        customerRepository.save(customer);

        // when - user role attempts delete
        mockMvc.perform(delete("/api/v1/customers/{id}", customer.getId())
                        .header("Authorization", "Bearer user-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void givenCustomerSearchCriteriaDTO_whenGetAllCustomersUsingPagination_thenReturnCustomerDTOPage() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer);

        Customer customer1 = new Customer();
        customer1.setFirstName("Sarah");
        customer1.setLastName("Wick");
        customer1.setEmail("swick@tester.com");
        customer1.setPhoneNumber("0123458881");
        customer1.setDateOfBirth(LocalDate.now().minusYears(18));

        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Maria");
        customer2.setLastName("Smith");
        customer2.setEmail("msmith@gmail.com");
        customer2.setPhoneNumber("5553338881");
        customer2.setDateOfBirth(LocalDate.now().minusYears(18));

        List<Customer> customerList = Arrays.asList(customer, customer2);
        customerRepository.saveAll(customerList);

        CustomerSearchCriteriaDTO customerSearchCriteriaDTO = new CustomerSearchCriteriaDTO();
        customerSearchCriteriaDTO.setPage(0);
        customerSearchCriteriaDTO.setSize(10);
        customerSearchCriteriaDTO.setFirstName("Maria");

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/customers/search")
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerSearchCriteriaDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.content.size()", is(1)))
                .andExpect(jsonPath("$.results.content[0].firstName", is(customer2.getFirstName())))
                .andExpect(jsonPath("$.results.content[0].lastName", is(customer2.getLastName())))
                .andExpect(jsonPath("$.results.content[0].email", is(customer2.getEmail())))
                .andExpect(jsonPath("$.results.content[0].phoneNumber", is("*******881")));
    }

    @Test
    void givenNoAuthentication_whenGetCustomer_thenUnauthorized() throws Exception {
        // given - unauthenticated request (no Authorization header)
        
        // when - action or behaviour that we are going to test
        ResultActions responseNoAuth = mockMvc.perform(get("/api/v1/customers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        responseNoAuth.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenAuthWithoutRoles_whenGetCustomer_thenForbidden() throws Exception {
        // given - authenticated request without roles
        
        // when - action or behaviour that we are going to test
        ResultActions responseForbidden = mockMvc.perform(get("/api/v1/customers/{id}", 1L)
                .header("Authorization", "Bearer no-roles-token")
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        responseForbidden.andDo(print())
                .andExpect(status().isForbidden());
    }
}

