package com.cafe.backend.service;

import com.cafe.backend.dto.CustomerOrderSummaryResponse;
import com.cafe.backend.dto.CustomerRequest;
import com.cafe.backend.dto.CustomerResponse;
import com.cafe.backend.entity.Customer;
import com.cafe.backend.exception.ResourceNotFoundException;
import com.cafe.backend.repository.CustomerRepository;
import com.cafe.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .notes(request.getNotes())
                .active(true)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponse.builder()
                .id(savedCustomer.getId())
                .name(savedCustomer.getName())
                .phone(savedCustomer.getPhone())
                .notes(savedCustomer.getNotes())
                .active(savedCustomer.getActive())
                .createdAt(savedCustomer.getCreatedAt())
                .build();
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findByActiveTrue()
                .stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .notes(customer.getNotes())
                        .active(customer.getActive())
                        .createdAt(customer.getCreatedAt())
                        .build())
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .notes(customer.getNotes())
                .active(customer.getActive())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setNotes(request.getNotes());

        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerResponse.builder()
                .id(updatedCustomer.getId())
                .name(updatedCustomer.getName())
                .phone(updatedCustomer.getPhone())
                .notes(updatedCustomer.getNotes())
                .active(updatedCustomer.getActive())
                .createdAt(updatedCustomer.getCreatedAt())
                .build();
    }

    public void deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setActive(false);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .notes(customer.getNotes())
                        .active(customer.getActive())
                        .createdAt(customer.getCreatedAt())
                        .build())
                .toList();
    }

    public List<CustomerOrderSummaryResponse> getCustomerOrders(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return orderRepository.findByCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(order -> CustomerOrderSummaryResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .cashierName(order.getUser().getUsername())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }
}