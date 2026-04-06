package com.cafe.backend.repository;

import com.cafe.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByActiveTrue();
    List<Customer> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    long countByActiveTrue();
}