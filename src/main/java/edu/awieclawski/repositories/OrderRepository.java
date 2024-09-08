package edu.awieclawski.repositories;

import edu.awieclawski.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderNo(String orderNo);

    @Query(value = "SELECT COUNT(id) FROM orders WHERE to_char(created_at, 'YYYY-MM-DD') = :stringDate GROUP BY DATE_TRUNC('DAY', created_at)", nativeQuery = true)
    Integer countOrdersByStringDate(@Param("stringDate") String stringDate);

}