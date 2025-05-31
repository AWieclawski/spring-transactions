package edu.awieclawski.controllers;

import edu.awieclawski.dtos.OrderPositionDto;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.exceptions.RestException;
import edu.awieclawski.services.OrderPositionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "positions/")
public class PositionsController {

    private final OrderPositionService orderPositionService;

    @PostMapping(path = "bypositionnames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> byOrder(@RequestBody List<String> orderIds,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size
    ) {
        checkObject(orderIds);
        Map<String, Object> positionsMap = orderPositionService.getByPositionNames(orderIds, page, size);
        return handleResultsMap(positionsMap, "No Position with position names: " + orderIds);
    }

    @GetMapping(path = "all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderPositionDto>> getAll() {
        return handleResultsList(orderPositionService.getAllPositions(), "No Positions");
    }

    private void checkObject(Object object) {
        if (object == null) {
            throw new RestException("No entity in the body!");
        }
    }

    private ResponseEntity<Map<String, Object>> handleResultsMap(Map<String, Object> resultMap, String errMsg) {
        if (MapUtils.isNotEmpty(resultMap)) {
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }

    private ResponseEntity<List<OrderPositionDto>> handleResultsList(List<OrderPositionDto> orderPositionDtos, String errMsg) {
        if (CollectionUtils.isNotEmpty(orderPositionDtos)) {
            return new ResponseEntity<>(orderPositionDtos, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }
}
