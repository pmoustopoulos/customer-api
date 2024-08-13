package com.ainigma100.customerapi.dto;


import com.ainigma100.customerapi.utils.annotation.ValidDateOfBirth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @NotEmpty(message = "firstName should not be null or empty")
    @Size(min = 2, message = "firstName should have at least 2 characters")
    private String firstName;

    @NotEmpty(message = "lastName should not be null or empty")
    @Size(min = 2, message = "lastName should have at least 2 characters")
    private String lastName;

    @NotEmpty(message = "email should not be null or empty")
    @Email(message = "email should be valid")
    private String email;


    /**
     * This validation ensures that the phone number has exactly 10 characters.
     * You can adjust the min and max attributes based on your requirements.
     */
    @Size(min = 10, max = 10, message = "phoneNumber should have exactly 10 characters")
    private String phoneNumber;


    /**
     * This custom validation ensures that the date of birth is not in the future and
     * the customer is at least the specified minimum age.
     */
    @NotNull(message = "dateOfBirth should not be null")
    @ValidDateOfBirth(minAge = 18)
    private LocalDate dateOfBirth;

}
