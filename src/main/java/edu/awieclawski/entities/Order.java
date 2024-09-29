package edu.awieclawski.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(of = {"orderNo", "orderDate"}, callSuper = true)
@Table(name = Order.TABLE_NAME)
public class Order extends BaseEntity {

    public static final String TABLE_NAME = "orders";

    public static final int PADDING = 3;

    public static final String ORDER_PATTERN = "yyyy-MM-dd";

    public static final DateTimeFormatter ORDER_FORMATTER = DateTimeFormatter.ofPattern(ORDER_PATTERN)
            .withZone(ZoneId.systemDefault());

    @Column(name = "order_no", updatable = false)
    private String orderNo;

    @Setter
    @Column(name = "order_date")
    private Instant orderDate;

    @Setter
    @Embedded
    private Contact contact;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderPosition> positions;

    @Setter
    @Transient
    private BigDecimal orderAmount;

    @Override
    List<String> getVerificationFields() {
        return List.of("orderNo");
    }

    public void assignOrderNo(int counter, String stringDate) {
        this.orderNo = String.format("%0" + Order.PADDING + "d-%s", counter, stringDate);
    }
}
