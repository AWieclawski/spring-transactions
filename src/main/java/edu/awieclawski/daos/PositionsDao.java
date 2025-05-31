package edu.awieclawski.daos;

import edu.awieclawski.entities.OrderPosition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PositionsDao {

    Page<OrderPosition> getByPositionNames(List<String> names, Integer page, Integer size);
}
