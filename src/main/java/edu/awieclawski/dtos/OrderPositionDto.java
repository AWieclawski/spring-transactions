package edu.awieclawski.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.awieclawski.enums.UoM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPositionDto {

    private String description;

    private BigDecimal quantity;

    private BigDecimal unitValue;

    private UoM uom;

    private String orderNo; // imported OrderPositionDto not allowed to update Order
}
