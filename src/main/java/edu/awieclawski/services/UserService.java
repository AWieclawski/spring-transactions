package edu.awieclawski.services;

import edu.awieclawski.dtos.UserDto;
import edu.awieclawski.entities.Order;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user);

    List<UserDto> getUsersByCountryNames(List<String> countryNames);

    Map<String, List<UserDto>> getUsersMapByCountryNames(List<String> countryNames);

    List<UserDto> getUsersByCityNames(List<String> cityNames);

    Map<String, List<UserDto>> getUsersMapByCityNames(List<String> cityNames);

    List<UserDto> getUsersByLastNames(List<String> lastNames);

    List<UserDto> getUsersByEmailContent(String content);

    List<UserDto> getUsersByEmails(List<String> emails);

    void assignUserToOrderByLogin(Order order, String userLogin);
}
