package com.cafe.backend.repository;

import com.cafe.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByActiveTrue();

    List<Order> findByCustomerIdAndActiveTrue(Long customerId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.active = true")
    BigDecimal findTotalSalesByActiveTrue();

    long countByActiveTrue();

    @Query("""
        SELECT FUNCTION('DATE', o.createdAt), COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.active = true
        GROUP BY FUNCTION('DATE', o.createdAt)
        ORDER BY FUNCTION('DATE', o.createdAt)
    """)
    List<Object[]> findDailySalesSummary();

    @Query("""
        SELECT FUNCTION('DATE', o.createdAt), COUNT(o)
        FROM Order o
        WHERE o.active = true
        GROUP BY FUNCTION('DATE', o.createdAt)
        ORDER BY FUNCTION('DATE', o.createdAt)
    """)
    List<Object[]> findDailyOrdersSummary();

    @Query("""
        SELECT o
        FROM Order o
        LEFT JOIN o.customer c
        LEFT JOIN o.user u
        WHERE o.active = true
          AND (:keyword IS NULL
               OR LOWER(COALESCE(c.name, 'walk-in customer')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(u.username, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR CAST(o.id AS string) LIKE CONCAT('%', :keyword, '%'))
          AND (:fromDate IS NULL OR o.createdAt >= :fromDate)
          AND (:toDate IS NULL OR o.createdAt < :toDate)
        ORDER BY o.createdAt DESC
    """)
    List<Order> searchArchive(String keyword, LocalDateTime fromDate, LocalDateTime toDate);
}