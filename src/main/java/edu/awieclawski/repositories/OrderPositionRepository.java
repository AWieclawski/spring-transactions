package edu.awieclawski.repositories;

import edu.awieclawski.entities.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {

    @Query(value = "SELECT op FROM OrderPosition op WHERE op.order.orderNo = :orderNo AND op.position.description = :description ")
    OrderPosition findOrderPositionByOrderNoAndDescription(@Param("orderNo") String orderNo, @Param("description") String description);
}