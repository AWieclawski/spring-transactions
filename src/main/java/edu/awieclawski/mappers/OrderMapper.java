package edu.awieclawski.mappers;

import edu.awieclawski.dtos.OrderDto;
import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.entities.Contact;
import edu.awieclawski.entities.Order;
import edu.awieclawski.entities.OrderPosition;
import edu.awieclawski.utils.MapUtils;
import edu.awieclawski.utils.PositionsHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static PositionsHandler update(OrderDto source, Order target) {
        target.setOrderDate(source.getOrderDate());
        assignContact(source, target);
        return updatePositions(source, target);
    }

    public static Order toEntity(OrderDto dto) {
        return Order.builder()
                // User is not to be set in the Mapper
                .orderNo(dto.getOrderNo())
                .orderDate(dto.getOrderDate())
                .contact(toContact(dto))
                .build();
    }

    public static OrderDto toDto(Order entity) {
        OrderDto dto = null;
        Contact contact = entity != null ? entity.getContact() : null;
        if (contact != null) { // must be some contact
            dto = OrderDto.builder()
                    .userLogin(entity.getUser() != null ? entity.getUser().getLogin() : null)
                    .email(contact.getEmail())
                    .phone(contact.getPhone())
                    .orderDate(entity.getOrderDate())
                    .orderNo(entity.getOrderNo())
                    .positions(new ArrayList<>())
                    .build();
            assignPositionsFromOrderDtoToNewOrder(entity, dto);
        }
        return dto;
    }

    public static List<OrderPosition> createOrderPositions(OrderDto source, Order target) {
        List<OrderPositionDto> dtoList = source.getPositions();
        List<OrderPosition> positions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            dtoList.forEach(dto -> {
                OrderPosition position = new OrderPosition(target);
                PositionsMapper.assignPosition(dto, position);
                positions.add(position);
            });
        }
        return positions;
    }

    public static PositionsHandler updatePositions(OrderDto source, Order target) {
        List<OrderPosition> positionsToRemove = new ArrayList<>();
        List<OrderPosition> newPositions = new ArrayList<>();
        String entityFieldName = "position.description";
        Map<String, OrderPosition> primaryPositions = MapUtils.getMapFromListByStringKeyField(target.getPositions(), entityFieldName);
        String dtoFieldName = "description";
        Map<String, List<OrderPositionDto>> actualPositions = MapUtils.getMapOfListsFromListByStringKeyField(source.getPositions(), dtoFieldName);
        actualPositions.forEach((k, v) -> {
            if (v.size() > 1) {
                throw new RuntimeException("Too many values " + v.size() + " for key: " + k);
            } else {
                OrderPositionDto actualPositionDto = v.get(0);
                OrderPosition foundPosition = primaryPositions.get(k);
                if (foundPosition != null) {
                    PositionsMapper.copyPosition(actualPositionDto, foundPosition);
                } else {
                    OrderPosition newPosition = new OrderPosition(target);
                    PositionsMapper.assignPosition(actualPositionDto, newPosition);
                    newPositions.add(newPosition);
                }
            }
        });
        primaryPositions.forEach((k, v) -> {
            if (!actualPositions.containsKey(k)) {
                positionsToRemove.add(v);
            }
        });
        return PositionsHandler.create(newPositions, positionsToRemove);
    }

    public static void assignPositionsFromOrderDtoToNewOrder(Order source, OrderDto target) {
        List<OrderPosition> positions = source.getPositions();
        if (CollectionUtils.isNotEmpty(positions)) {
            positions.forEach(positionEntity -> target.getPositions().add(PositionsMapper.toDto(positionEntity)));
        }
    }

    public static void assignContact(OrderDto source, Order target) {
        if (target.getContact() == null) {
            target.setContact(toContact(source));
        } else {
            target.getContact().setEmail(source.getEmail());
            target.getContact().setPhone(source.getPhone());
        }
    }

    public static Contact toContact(OrderDto dto) {
        return Contact.builder()
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}
