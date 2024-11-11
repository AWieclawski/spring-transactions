package edu.awieclawski.services;

import edu.awieclawski.dtos.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto);

    OrderDto updateOrder(OrderDto orderDto);

    List<OrderDto> getOrdersByPositionNames(List<String> positionNames);

    List<OrderDto> getOrdersByOrderNumbers(List<String> orderNumbers);

}
