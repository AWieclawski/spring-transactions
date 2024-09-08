package edu.awieclawski.utils;

import edu.awieclawski.entities.OrderPosition;
import lombok.Getter;

import java.util.List;

@Getter
public class PositionsHandler {

    private final List<OrderPosition> positionsToRemove;
    private final List<OrderPosition> newPositions;

    public PositionsHandler(List<OrderPosition> newPositions, List<OrderPosition> positionsToRemove) {
        this.positionsToRemove = positionsToRemove;
        this.newPositions = newPositions;
    }

    public static PositionsHandler create(List<OrderPosition> newPositions, List<OrderPosition> positionsToRemove) {
        return new PositionsHandler(newPositions, positionsToRemove);
    }
}
