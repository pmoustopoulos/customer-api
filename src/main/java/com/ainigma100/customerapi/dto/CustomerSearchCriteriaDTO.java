package com.ainigma100.customerapi.dto;


import com.ainigma100.customerapi.utils.SortItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class CustomerSearchCriteriaDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    @NotNull(message = "page cannot be null")
    @PositiveOrZero(message = "page must be a zero or a positive number")
    private Integer page;

    @Schema(example = "10")
    @NotNull(message = "size cannot be null")
    @Positive(message = "size must be a positive number")
    private Integer size;

    private List<SortItem> sortList;

}
