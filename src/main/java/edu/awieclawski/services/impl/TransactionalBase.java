package edu.awieclawski.services.impl;

import edu.awieclawski.entities.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.SQLException;

@RequiredArgsConstructor
abstract class TransactionalBase<T extends BaseEntity> {

    protected final JpaRepository<T, Long> baseRepository;

    protected abstract JpaRepository<T, Long> getBaseRepository();

    protected String getCtxTransactionName() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    protected void assignTransactionToEntity(BaseEntity entity) {
        entity.setTransactionName(getCtxTransactionName());
    }

    protected T saveNewEntity(T entity) throws SQLException {
        assignTransactionToEntity(entity);
        return baseRepository.save(entity);
    }
}
