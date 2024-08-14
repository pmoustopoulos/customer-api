package com.ainigma100.customerapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class Utils {

    // Private constructor to prevent instantiation
    private Utils() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Retrieves a value from a Supplier or sets a default value if a NullPointerException occurs.
     * Usage example:
     *
     * <pre>{@code
     * // Example 1: Retrieve a list or provide an empty list if null
     * List<Employee> employeeList = Utils.retrieveValueOrSetDefault(() -> someSupplierMethod(), new ArrayList<>());
     *
     * // Example 2: Retrieve an Employee object or provide a default object if null
     * Employee emp = Utils.retrieveValueOrSetDefault(() -> anotherSupplierMethod(), new Employee());
     * }</pre>
     *
     * @param supplier     the Supplier providing the value to retrieve
     * @param defaultValue the default value to return if a NullPointerException occurs
     * @return the retrieved value or the default value if a NullPointerException occurs
     * @param <T>          the type of the value
     */
    public static <T> T retrieveValueOrSetDefault(Supplier<T> supplier, T defaultValue) {

        try {
            return supplier.get();

        } catch (NullPointerException ex) {

            log.error("Error while retrieveValueOrSetDefault {}", ex.getMessage());

            return defaultValue;
        }
    }


    public static Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sortList != null) {
            // iterate the SortList to see based on which attributes we are going to Order By the results.
            for(SortItem sortValue : sortList) {
                orders.add(new Sort.Order(sortValue.getDirection(), sortValue.getField()));
            }
        }


        return PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10),
                Sort.by(orders));
    }

}
