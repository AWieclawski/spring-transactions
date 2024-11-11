package edu.awieclawski.daos;

import edu.awieclawski.entities.Order;

import java.util.List;

public interface OrdersDao {

    List<Order> getByPositionNames(List<String> names);

    List<Order> getByOrderNumbers(List<String> names);
}
