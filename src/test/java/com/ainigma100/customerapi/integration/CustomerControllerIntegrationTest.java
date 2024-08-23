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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("testcontainers")
class CustomerControllerIntegrationTest extends AbstractContainerBaseTest {

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
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isCreated())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(customerRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customerRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customerRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is(customerRequestDTO.getPhoneNumber())))
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
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", equalTo(Math.toIntExact(customer.getId()))))
                .andExpect(jsonPath("$.results.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customer.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customer.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is(customer.getPhoneNumber())))
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

        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("Johnathan");
        customerRequestDTO.setLastName("Wick");
        customerRequestDTO.setEmail("jw@gmail.com");
        customerRequestDTO.setPhoneNumber("4444444444");
        customerRequestDTO.setDateOfBirth(LocalDate.now().minusYears(21));


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", equalTo(Math.toIntExact(customer.getId()))))
                .andExpect(jsonPath("$.results.firstName", is(customerRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(customerRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(customerRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.phoneNumber", is(customerRequestDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.results.dateOfBirth", is(customerRequestDTO.getDateOfBirth().toString())));
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
        customerEmailUpdateDTO.setEmail("loco@gmail.com");


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(patch("/api/v1/customers/{id}/email", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerEmailUpdateDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", is(1)))
                .andExpect(jsonPath("$.results.firstName", is("John")))
                .andExpect(jsonPath("$.results.lastName", is("Wick")))
                .andExpect(jsonPath("$.results.email", is("loco@gmail.com")))
                .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
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
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));
    }


    @Test
    void givenCustomerSearchCriteriaDTO_whenGetAllCustomersUsingPagination_thenReturnCustomerDTOPage() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@gmail.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(21));

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
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerSearchCriteriaDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.content.size()", is(1)))
                .andExpect(jsonPath("$.results.content[0].firstName", is(customer2.getFirstName())))
                .andExpect(jsonPath("$.results.content[0].lastName", is(customer2.getLastName())))
                .andExpect(jsonPath("$.results.content[0].email", is(customer2.getEmail())))
                .andExpect(jsonPath("$.results.content[0].phoneNumber", is(customer2.getPhoneNumber())));
    }

}
