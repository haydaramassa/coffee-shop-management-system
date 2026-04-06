package com.cafe.backend.controller;

import com.cafe.backend.dto.CustomerOrderSummaryResponse;
import com.cafe.backend.dto.CustomerRequest;
import com.cafe.backend.dto.CustomerResponse;
import com.cafe.backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public String deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
        return "Customer deactivated successfully";
    }

    @GetMapping("/search")
    public List<CustomerResponse> searchCustomersByName(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }

    @GetMapping("/{id}/orders")
    public List<CustomerOrderSummaryResponse> getCustomerOrders(@PathVariable Long id) {
        return customerService.getCustomerOrders(id);
    }
}