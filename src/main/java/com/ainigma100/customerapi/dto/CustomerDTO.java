package com.ainigma100.customerapi.dto;

//import com.ainigma100.customerapi.utils.annotation.MaskData;
import com.ainigma100.customerapi.utils.annotation.MaskData;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @MaskData(visibleCharactersAtEnd = 3, maskSymbol = "*")
    private String phoneNumber;

    private LocalDate dateOfBirth;

}
