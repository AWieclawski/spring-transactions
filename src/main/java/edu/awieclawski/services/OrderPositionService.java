package edu.awieclawski.services;

import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.entities.OrderPosition;

import java.util.List;
import java.util.Map;

public interface OrderPositionService {

    OrderPositionDto savePosition(OrderPositionDto positionDto);

    void savePositionsToOrder(List<OrderPosition> orderPositions);

    void deletePositionsFromOrder(List<OrderPosition> orderPositions);

    OrderPositionDto updatePosition(OrderPositionDto positionDto);

    Map<String, Object> getByPositionNames(List<String> porderIds, Integer page, Integer size);

    List<OrderPositionDto> getAllPositions();

}
