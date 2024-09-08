package edu.awieclawski.daos;

import edu.awieclawski.entities.User;

import java.util.List;

public interface UsersDao {

    List<User> getByCountryNamesInAddress(List<String> countryNames);

    List<User> getByCityNamesInAddress(List<String> countryNames);

    List<User> getByLastNamesInUser(List<String> lastNames);

    List<User> findByEmailContentInUser(String content);

    List<User> findByEmailsInUser(List<String> emails);
}
