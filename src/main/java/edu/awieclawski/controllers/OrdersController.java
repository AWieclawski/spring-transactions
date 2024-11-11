package edu.awieclawski.controllers;

import edu.awieclawski.dtos.OrderDto;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.exceptions.RestException;
import edu.awieclawski.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "orders/")
public class OrdersController {

    private final OrderService orderService;

    @PostMapping(path = "save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto newOrder) {
        checkObject(newOrder);
        OrderDto orderDto = orderService.saveOrder(newOrder);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        checkObject(orderDto);
        OrderDto userDto = orderService.updateOrder(orderDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "bypositionnames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> byPositionNames(@RequestBody List<String> positionNames) {
        checkObject(positionNames);
        List<OrderDto> orders = orderService.getOrdersByPositionNames(positionNames);
        return handleResultsList(orders, "No Order with position names: " + positionNames);
    }

    @PostMapping(path = "byordernumbers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> byOrderNumbers(@RequestBody List<String> orderNumbers) {
        checkObject(orderNumbers);
        List<OrderDto> orders = orderService.getOrdersByOrderNumbers(orderNumbers);
        return handleResultsList(orders, "No Order with order numbers: " + orderNumbers);
    }


    private void checkObject(Object object) {
        if (object == null) {
            throw new RestException("No entity in the body!");
        }
    }

    private ResponseEntity<List<OrderDto>> handleResultsList(List<OrderDto> userDtoList, String errMsg) {
        if (CollectionUtils.isNotEmpty(userDtoList)) {
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }

    private ResponseEntity<Map<String, List<OrderDto>>> handleResultsMap(Map<String, List<OrderDto>> usersMap, String errMsg) {
        if (MapUtils.isNotEmpty(usersMap)) {
            return new ResponseEntity<>(usersMap, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }
}