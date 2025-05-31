package edu.awieclawski.daos.impl;

import edu.awieclawski.daos.PositionsDao;
import edu.awieclawski.entities.OrderPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PositionsDaoImpl extends CriteriaBuilderBase<OrderPosition> implements PositionsDao {

    @Override
    public Page<OrderPosition> getByPositionNames(List<String> names, Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return findByPropertyNameValuesPaged("position.description", names, paging);
    }

    public PositionsDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setEntityClazz();
    }

    @Override
    protected void setEntityClazz() {
        setEntityClazz(OrderPosition.class);
    }
}
