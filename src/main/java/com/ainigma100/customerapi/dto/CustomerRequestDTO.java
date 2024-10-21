package com.ainigma100.customerapi.dto;


import com.ainigma100.customerapi.utils.annotation.ValidDateOfBirth;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Customer's first name", example = "John")
    @NotEmpty(message = "firstName should not be null or empty")
    @Size(min = 2, message = "firstName should have at least 2 characters")
    private String firstName;

    @Schema(description = "Customer's last name", example = "Wick")
    @NotEmpty(message = "lastName should not be null or empty")
    @Size(min = 2, message = "lastName should have at least 2 characters")
    private String lastName;

    @Schema(description = "Customer's email address", example = "jwick@tester.com")
    @NotEmpty(message = "email should not be null or empty")
    // Email regex for better validation was found here: https://stackoverflow.com/a/65371615/5719310
    @Email(message = "Invalid email format", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;


    /**
     * This validation ensures that the phone number has exactly 10 characters.
     * You can adjust the min and max attributes based on your requirements.
     */
    @Schema(description = "Customer's phone number, must be exactly 10 characters", example = "6981234567")
    @Size(min = 10, max = 10, message = "phoneNumber should have exactly 10 characters")
    private String phoneNumber;


    /**
     * This custom validation ensures that the date of birth is not in the future and
     * the customer is at least the specified minimum age.
     */
    @Schema(description = "Customer's date of birth in format YYYY-MM-DD. Must not be in the future and the " +
            "customer should meet the minimum age requirement.",
            example = "1989-01-02", type = "string", format = "date")
    @NotNull(message = "dateOfBirth should not be null")
    @ValidDateOfBirth(minAge = 18)
    private LocalDate dateOfBirth;

}
