package edu.awieclawski.services.impl;

import edu.awieclawski.daos.PositionsDao;
import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.entities.OrderPosition;
import edu.awieclawski.mappers.PositionsMapper;
import edu.awieclawski.repositories.OrderPositionRepository;
import edu.awieclawski.services.OrderPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Do not inject OrderService, can cause Stack OverFlow as a feedback
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderPositionServiceImpl extends TransactionalBase<OrderPosition> implements OrderPositionService {

    private final PositionsDao positionsDao;

    public OrderPositionServiceImpl(OrderPositionRepository positionRepository,
                                    PositionsDao positionsDao) {
        super(positionRepository);
        this.positionsDao = positionsDao;
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

    @Override
    public Map<String, Object> getByPositionNames(List<String> positionNames, Integer page, Integer size) {
        try {
            Page<OrderPosition> pagePositions = getPositionsByPositionNames(positionNames, page, size);
            return mapPagePositionsToMap(pagePositions);
        } catch (Exception e) {
            log.error("Get Page by positions names failed! {}", e.getMessage());
            return getRawResponseMap();
        }
    }

    @Override
    public List<OrderPositionDto> getAllPositions() {
        return getBaseRepository().findAll().stream()
                .map(PositionsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderPositionRepository getBaseRepository() {
        return ((OrderPositionRepository) baseRepository);
    }

    private Page<OrderPosition> getPositionsByPositionNames(List<String> positionNames, Integer page, Integer size) {
        return positionsDao.getByPositionNames(positionNames, page, size);
    }

    private Map<String, Object> mapPagePositionsToMap(Page<OrderPosition> pagePositions) {
        List<OrderPosition> positions = pagePositions.getContent();
        List<OrderPositionDto> positionDtos = positions.stream()
                .map(PositionsMapper::toDto)
                .collect(Collectors.toList());
        var response = getRawResponseMap();
        response.put("positions", positionDtos);
        response.put("currentPage", pagePositions.getNumber());
        response.put("totalItems", pagePositions.getTotalElements());
        response.put("totalPages", pagePositions.getTotalPages());
        return response;
    }

    private OrderPosition saveNewPosition(OrderPosition position) {
        try {
            return saveNewEntity(position);
        } catch (SQLException e) {
            log.error("Position save failed! {} ", e.getMessage());
        }
        return null;
    }

    private Map<String, Object> getRawResponseMap() {
        return new HashMap<>();
    }

}
