package edu.awieclawski.entities;

import edu.awieclawski.enums.UoM;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@SuperBuilder
@Embeddable
@ToString(of = {"description", "quantity", "unitValue"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Position {

    private String description;

    private BigDecimal quantity;

    @Column(name = "unit_value")
    private BigDecimal unitValue;

    @Enumerated(EnumType.STRING)
    private UoM measure;

    @Transient
    private BigDecimal value;

    @Transient
    private Integer positionNo;

}
