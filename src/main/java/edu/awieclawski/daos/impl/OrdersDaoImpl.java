package edu.awieclawski.daos.impl;

import edu.awieclawski.daos.OrdersDao;
import edu.awieclawski.entities.Order;
import edu.awieclawski.entities.OrderPosition;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class OrdersDaoImpl extends CriteriaBuilderBase<Order> implements OrdersDao {

    public OrdersDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setEntityClazz();
    }

    @Override
    protected void setEntityClazz() {
        setEntityClazz(Order.class);
    }


    @Override
    public List<Order> getByPositionNames(List<String> names) {
        return findBySubPropertyValuesExtended("positions", OrderPosition.class, "position.description", names);
    }

    @Override
    public List<Order> getByOrderNumbers(List<String> names) {
        return findByPropertyNameValues("orderNo", names);
    }
}
