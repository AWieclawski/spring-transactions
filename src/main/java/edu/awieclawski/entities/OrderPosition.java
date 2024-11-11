package edu.awieclawski.entities;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(of = {"order"}, callSuper = true)
@Table(name = OrderPosition.TABLE_NAME)
public class OrderPosition extends BaseEntity {

    public static final String TABLE_NAME = "positions";

    @Embedded
    private Position position;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderPosition(Order order) {
        this.order = order;
    }

    @Override
    List<String> getVerificationFields() {
        return List.of("order.orderNo", "position.description");
    }
}
