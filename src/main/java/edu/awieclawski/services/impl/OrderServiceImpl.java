package edu.awieclawski.services.impl;

import edu.awieclawski.daos.OrdersDao;
import edu.awieclawski.dtos.OrderDto;
import edu.awieclawski.entities.Order;
import edu.awieclawski.entities.OrderPosition;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.mappers.OrderMapper;
import edu.awieclawski.repositories.OrderRepository;
import edu.awieclawski.services.OrderPositionService;
import edu.awieclawski.services.OrderService;
import edu.awieclawski.services.UserService;
import edu.awieclawski.utils.InstantUtils;
import edu.awieclawski.utils.PositionsHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderServiceImpl extends TransactionalBase<Order> implements OrderService {

    private final UserService userService;
    private final OrderPositionService orderPositionService;

    private final OrdersDao ordersDao;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, OrderPositionService orderPositionService, OrdersDao ordersDao) {
        super(orderRepository);
        this.userService = userService;
        this.orderPositionService = orderPositionService;
        this.ordersDao = ordersDao;
    }

    @Override
    public OrderRepository getBaseRepository() {
        return ((OrderRepository) baseRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto saveOrder(OrderDto orderDto) {
        Order entity = OrderMapper.toEntity(orderDto);
        userService.assignUserToOrderByLogin(entity, orderDto.getUserLogin());
        assignOrderNo(entity);
        assignNewOrderPositions(orderDto, entity);
        Order saved = saveNewOrder(entity, 0);
        return OrderMapper.toDto(saved);
    }

    private void assignNewOrderPositions(OrderDto orderDto, Order saved) {
        List<OrderPosition> positions = OrderMapper.createOrderPositions(orderDto, saved);
        if (CollectionUtils.isEmpty(saved.getPositions())) {
            saved.setPositions(new ArrayList<>());
        }
        saved.getPositions().addAll(positions);
        // positions saved by hibernate (cascade persist)
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto updateOrder(OrderDto orderDto) {
        Order foundOrder = getBaseRepository().findByOrderNo(orderDto.getOrderNo())
                .stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Order not found by order no: " + orderDto.getOrderNo()));
        PositionsHandler handler = OrderMapper.update(orderDto, foundOrder);
        updateOrderByHandler(foundOrder, handler);
        return OrderMapper.toDto(foundOrder);
    }

    private void updateOrderByHandler(Order foundOrder, PositionsHandler handler) {
        if (CollectionUtils.isNotEmpty(handler.getNewPositions())) {
            orderPositionService.savePositionsToOrder(handler.getNewPositions());
            foundOrder.getPositions().addAll(handler.getNewPositions());
        }
        if (CollectionUtils.isNotEmpty(handler.getPositionsToRemove())) {
            orderPositionService.deletePositionsFromOrder(handler.getPositionsToRemove());
            foundOrder.getPositions().removeAll(handler.getPositionsToRemove());
        }
    }

    @Override
    public List<OrderDto> getOrdersByPositionNames(List<String> positionNames) {
        List<Order> orders = ordersDao.getByPositionNames(positionNames);
        return orders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByOrderNumbers(List<String> orderNumbers) {
        List<Order> orders = ordersDao.getByOrderNumbers(orderNumbers);
        return orders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }

    private Order saveNewOrder(Order order, int retryCount) {
        while (retryCount <= 3) {
            try {
                return saveNewEntity(order);
            } catch (SQLException e) {
                log.error("{}. Order save failed! {} ", ++retryCount, e.getMessage());
                assignOrderNo(order);
                saveNewOrder(order, ++retryCount);
            }
        }
        return null;
    }

    private void assignOrderNo(Order order) {
        final String stringDate = InstantUtils.getCurrentTimestampAsString(Order.ORDER_FORMATTER);
        Integer counter = getBaseRepository().countOrdersByStringDate(stringDate);
        counter = counter != null ? counter : 0;
        order.assignOrderNo(++counter, stringDate);
    }
}
