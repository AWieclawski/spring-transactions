package edu.awieclawski.mappers;

import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.entities.OrderPosition;
import edu.awieclawski.entities.Position;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionsMapper {

    public static void update(OrderPositionDto source, OrderPosition target) {
        assignPosition(source, target);
    }

    public static OrderPosition toNewEntity(OrderPositionDto dto) {
        // Order is not to be set in the Mapper
        return OrderPosition.builder()
                .position(toPosition(dto))
                .build();
    }

    public static OrderPositionDto toDto(OrderPosition entity) {
        Position position = entity != null ? entity.getPosition() : null;
        if (position != null && entity.getOrder() != null) {
            return OrderPositionDto.builder()
                    .orderNo(entity.getOrder().getOrderNo())
                    .description(position.getDescription())
                    .unitValue(position.getUnitValue())
                    .quantity(position.getQuantity())
                    .uom(position.getMeasure())
                    .build();
        } else {
            return OrderPositionDto.builder()
                    .build();
        }
    }

    public static void assignPosition(OrderPositionDto source, OrderPosition target) {
        if (target.getPosition() == null) {
            target.setPosition(toPosition(source));
        } else {
            target.getPosition().setDescription(source.getDescription());
            target.getPosition().setUnitValue(source.getUnitValue());
            target.getPosition().setQuantity(source.getQuantity());
            target.getPosition().setMeasure(source.getUom());
        }
    }

    public static void copyPosition(OrderPositionDto source, OrderPosition target) {
        // can not affect description
        if (target.getPosition() == null) {
            target.setPosition(Position.builder().build());
        }
        target.getPosition().setUnitValue(source.getUnitValue());
        target.getPosition().setQuantity(source.getQuantity());
        target.getPosition().setMeasure(source.getUom());
    }

    public static Position toPosition(OrderPositionDto dto) {
        return Position.builder()
                .description(dto.getDescription())
                .unitValue(dto.getUnitValue())
                .quantity(dto.getQuantity())
                .measure(dto.getUom())
                .build();
    }
}
