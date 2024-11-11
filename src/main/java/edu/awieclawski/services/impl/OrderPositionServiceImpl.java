package edu.awieclawski.services.impl;

import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.entities.OrderPosition;
import edu.awieclawski.mappers.PositionsMapper;
import edu.awieclawski.repositories.OrderPositionRepository;
import edu.awieclawski.services.OrderPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Do not inject OrderService, can cause Stack OverFlow as a feedback
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderPositionServiceImpl extends TransactionalBase<OrderPosition> implements OrderPositionService {

    public OrderPositionServiceImpl(OrderPositionRepository positionRepository) {
        super(positionRepository);
    }

    @Override
    public OrderPositionRepository getBaseRepository() {
        return ((OrderPositionRepository) baseRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderPositionDto savePosition(OrderPositionDto positionDto) {
        OrderPosition orderPosition = PositionsMapper.toNewEntity(positionDto);
        OrderPosition saved = saveNewPosition(orderPosition);
        return PositionsMapper.toDto(saved);
    }

    @Override
    public void savePositionsToOrder(List<OrderPosition> orderPositions) {
        orderPositions.forEach(this::saveNewPosition);
    }

    @Override
    public void deletePositionsFromOrder(List<OrderPosition> orderPositions) {
        orderPositions.forEach(pz -> getBaseRepository().delete(pz));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderPositionDto updatePosition(OrderPositionDto positionDto) {
        String orderNo = positionDto.getOrderNo();
        OrderPosition foundPosition = getBaseRepository().findOrderPositionByOrderNoAndDescription(orderNo, positionDto.getDescription());
        PositionsMapper.update(positionDto, foundPosition);
        return PositionsMapper.toDto(foundPosition);
    }

    private OrderPosition saveNewPosition(OrderPosition position) {
        try {
            return saveNewEntity(position);
        } catch (SQLException e) {
            log.error("Position save failed! {} ", e.getMessage());
        }
        return null;
    }


}
