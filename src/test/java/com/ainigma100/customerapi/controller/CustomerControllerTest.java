package com.ainigma100.customerapi.controller;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerRequestDTO;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerMapper customerMapper;

    private CustomerRequestDTO customerRequestDTO;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {

        customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("John");
        customerRequestDTO.setLastName("Wick");
        customerRequestDTO.setEmail("jwick@tester.com");
        customerRequestDTO.setPhoneNumber("0123456789");
        customerRequestDTO.setDateOfBirth(LocalDate.now().minusYears(18));


        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Wick");
        customerDTO.setEmail("jwick@tester.com");
        customerDTO.setPhoneNumber("0123456789");
        customerDTO.setDateOfBirth(LocalDate.now().minusYears(18));

    }


    @Test
    void givenCustomerDTO_whenCreateCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        given(customerMapper.customerRequestDTOToCustomerDTO(any(CustomerRequestDTO.class)))
                .willReturn(customerDTO);

        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

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
                .andExpect(jsonPath("$.results.id", is(1)))
                .andExpect(jsonPath("$.results.firstName", is("John")))
                .andExpect(jsonPath("$.results.lastName", is("Wick")))
                .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
                .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
    }


    @Test
    void givenCustomerDTO_whenGetCustomerById_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        given(customerService.getCustomerById(any(Long.class))).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

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
                .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
                .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
    }


    @Test
    void givenCustomerDTO_whenUpdateCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        given(customerMapper.customerRequestDTOToCustomerDTO(any(CustomerRequestDTO.class)))
                .willReturn(customerDTO);

        given(customerService.updateCustomer(any(Long.class), any(CustomerDTO.class))).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)));

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
                .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
                .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
                .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
    }


    @Test
    void givenCustomerDTO_whenDeleteCustomer_thenReturnCustomerDTO() throws Exception {

        // given - precondition or setup
        willDoNothing().given(customerService).deleteCustomer(any(Long.class));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/customers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));
    }


}
