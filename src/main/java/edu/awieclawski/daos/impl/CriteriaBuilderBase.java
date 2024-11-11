package edu.awieclawski.daos.impl;

import edu.awieclawski.entities.BaseEntity;
import edu.awieclawski.utils.ReflectionUtils;
import io.hypersistence.utils.hibernate.query.SQLExtractor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Related knowledge sources:
 * https://www.baeldung.com/hibernate-criteria-queries
 * https://www.baeldung.com/spring-data-criteria-queries
 * https://www.baeldung.com/jpa-criteria-api-in-expressions
 * https://stackoverflow.com/questions/42530677/jpa-criteria-builder-in-clause-query
 * https://developer.ibm.com/articles/j-typesafejpa/
 */
@Slf4j
abstract class CriteriaBuilderBase<T extends BaseEntity> {
    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;
    protected Class<T> entityClazz;

    protected CriteriaBuilderBase(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    protected abstract void setEntityClazz();

    protected void setEntityClazz(Class<T> entityClazz) {
        this.entityClazz = entityClazz;
    }

    protected List<T> findByPropertyNameValues(String propertyName, List<String> propertyValues) {
        final CriteriaQuery<T> criteriaQuery = createCriteriaQuery(entityClazz);
        final Root<T> rootEntity = criteriaQuery.from(entityClazz);
        if (ReflectionUtils.isFieldDeclaredInEmbeddedEntity(entityClazz, propertyName)) {
            criteriaQuery.select(rootEntity).where(handleEmbeddedEntity(rootEntity, propertyName).in(propertyValues));
            TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        }
        return List.of();
    }

    protected List<T> findByPropertyNameContent(String propertyName, String content) {
        final CriteriaQuery<T> criteriaQuery = createCriteriaQuery(entityClazz);
        final Root<T> rootEntity = criteriaQuery.from(entityClazz);
        if (ReflectionUtils.isFieldDeclaredInEmbeddedEntity(entityClazz, propertyName)) {
            criteriaQuery.select(rootEntity).where(criteriaBuilder.like(handleEmbeddedEntity(rootEntity, propertyName), ("%" + content + "%")));
            TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        }
        return List.of();
    }

    /**
     * Useful when property may be in Many-To-One relation to generic Entity <T>
     *
     * @param propertyName - field name in generic Entity <T>
     * @param subEntityClazz - Class of field in generic Entity <T>
     * @param subPropertyName - field name in subordinate Entity <S>
     * @param subPropertyValues - searched values field name in subordinate Entity <S>
     * @param <S> - type of subordinate Entity <S>
     * @return
     */
    protected <S extends BaseEntity> List<T> findBySubPropertyValuesExtended(String propertyName,
                                                                             Class<S> subEntityClazz,
                                                                             String subPropertyName,
                                                                             List<String> subPropertyValues) {
        final CriteriaQuery<T> criteriaQuery = createCriteriaQuery(entityClazz);
        final Root<T> rootEntity = criteriaQuery.from(entityClazz);
        criteriaQuery.select(rootEntity);

        if (ReflectionUtils.isFieldDeclared(entityClazz, propertyName)) {
            final Subquery<S> subQuery = criteriaQuery.subquery(subEntityClazz);
            final Root<S> rootSubEntity = subQuery.from(subEntityClazz);
            subQuery.select(rootSubEntity);

            if (ReflectionUtils.isFieldDeclaredInEmbeddedEntity(subEntityClazz, subPropertyName)) {
                Expression<String> rootSubExpression = handleEmbeddedEntity(rootSubEntity, subPropertyName);
                Predicate rootSubPredicate = rootSubExpression.in(subPropertyValues);
                subQuery.where(rootSubPredicate);
            }

            Class<?> propertyTypeClazz = ReflectionUtils.getFieldType(rootEntity.getModel().getJavaType(), propertyName);
            Predicate rootPredicate;

            if (List.class.isAssignableFrom(propertyTypeClazz)) {
                ListJoin<T, S> propertyEntities = rootEntity.joinList(propertyName);
                rootPredicate = criteriaBuilder.in(propertyEntities).value(subQuery);
            } else if (Set.class.isAssignableFrom(propertyTypeClazz)) {
                SetJoin<T, S> propertyEntities = rootEntity.joinSet(propertyName);
                rootPredicate = criteriaBuilder.in(propertyEntities).value(subQuery);
            } else if (Collection.class.isAssignableFrom(propertyTypeClazz)) {
                CollectionJoin<T, S> propertyEntities = rootEntity.joinCollection(propertyName);
                rootPredicate = criteriaBuilder.in(propertyEntities).value(subQuery);
            } else {
                Expression<String> rootExpression = handleEmbeddedEntity(rootEntity, propertyName);
                rootPredicate = rootExpression.in(subQuery);
            }

            criteriaQuery.where(rootPredicate);
            TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
            loggerSql(query);

            return query.getResultList();

        }
        return List.of();
    }

    private CriteriaQuery<T> createCriteriaQuery(Class<T> clazz) {
        return criteriaBuilder.createQuery(clazz);
    }

    private <Y> Path<Y> handleEmbeddedEntity(Root<?> rootEntity, String propertyName) {
        Path<Y> path;
        if (propertyName.contains(".")) {
            String[] embeddedProperty = propertyName.split("\\.");
            path = rootEntity.get(embeddedProperty[0]).get(embeddedProperty[1]);
        } else {
            path = rootEntity.get(propertyName);
        }
        return path;
    }


    private void loggerSql(Query jpql) {

        try {

            String sql = SQLExtractor.from(jpql);

            log.debug("The Criteria API, compiled to this JPQL query: \n[{}] generates the following SQL query: \n[{}]    ",
                    jpql.unwrap(org.hibernate.query.Query.class).getQueryString(),
                    sql
            );

        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }

    }

}
