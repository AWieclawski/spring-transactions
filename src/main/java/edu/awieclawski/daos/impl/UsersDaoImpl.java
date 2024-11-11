package edu.awieclawski.daos.impl;

import edu.awieclawski.daos.UsersDao;
import edu.awieclawski.entities.Address;
import edu.awieclawski.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UsersDaoImpl extends CriteriaBuilderBase<User> implements UsersDao {

    public UsersDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setEntityClazz();
    }

    @Override
    protected void setEntityClazz() {
        setEntityClazz(User.class);
    }

    @Override
    public List<User> getByCountryNamesInAddress(List<String> countryNames) {
        return findBySubPropertyValuesExtended("address", Address.class, "country", countryNames);
    }

    @Override
    public List<User> getByCityNamesInAddress(List<String> cityNames) {
        return findBySubPropertyValuesExtended("address", Address.class, "city", cityNames);
    }

    @Override
    public List<User> getByLastNamesInUser(List<String> lastNames) {
        return findByPropertyNameValues("lastName", lastNames);
    }

    @Override
    public List<User> findByEmailContentInUser(String content) {
        return findByPropertyNameContent("contact.email", content);
    }

    @Override
    public List<User> findByEmailsInUser(List<String> emails) {
        return findByPropertyNameValues("contact.email", emails);
    }
}
