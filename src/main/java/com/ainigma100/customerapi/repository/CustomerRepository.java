package com.ainigma100.customerapi.repository;

import com.ainigma100.customerapi.dto.CustomerSearchCriteriaDTO;
import com.ainigma100.customerapi.entity.Customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);


    @Query(value = """
            select cus from Customer cus
            where ( :#{#criteria.firstName} IS NULL OR LOWER(cus.firstName) LIKE LOWER( CONCAT(:#{#criteria.firstName}, '%') ) )
            and ( :#{#criteria.lastName} IS NULL OR LOWER(cus.lastName) LIKE LOWER( CONCAT(:#{#criteria.lastName}, '%') ) )
            and ( :#{#criteria.email} IS NULL OR LOWER(cus.email) LIKE LOWER( CONCAT('%', :#{#criteria.email}, '%') ) )
            and ( :#{#criteria.phoneNumber} IS NULL OR LOWER(cus.phoneNumber) LIKE LOWER( CONCAT('%', :#{#criteria.phoneNumber}, '%') ) )
            and ( :#{#criteria.dateOfBirth} IS NULL OR cus.dateOfBirth = :#{#criteria.dateOfBirth} )
            """)
    Page<Customer> getAllCustomersUsingPagination(
            @Param("criteria") CustomerSearchCriteriaDTO customerSearchCriteriaDTO,
            Pageable pageable);

}
