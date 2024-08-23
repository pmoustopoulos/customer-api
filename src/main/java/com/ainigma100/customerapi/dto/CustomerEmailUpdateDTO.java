package com.ainigma100.customerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEmailUpdateDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

}
