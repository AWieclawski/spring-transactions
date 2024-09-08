package edu.awieclawski.services.impl;

import edu.awieclawski.daos.UsersDao;
import edu.awieclawski.dtos.AddressDto;
import edu.awieclawski.dtos.UserDto;
import edu.awieclawski.entities.Address;
import edu.awieclawski.entities.Order;
import edu.awieclawski.entities.User;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.mappers.AddressMapper;
import edu.awieclawski.mappers.UserMapper;
import edu.awieclawski.repositories.UserRepository;
import edu.awieclawski.services.AddressService;
import edu.awieclawski.services.UserService;
import edu.awieclawski.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserServiceImpl extends TransactionalBase<User> implements UserService {

    private final AddressService addressService;

    private final UsersDao usersDao;

    public UserServiceImpl(UserRepository userRepository, AddressService addressService, UsersDao usersDao) {
        super(userRepository);
        this.addressService = addressService;
        this.usersDao = usersDao;
    }

    @Override
    public UserRepository getBaseRepository() {
        return ((UserRepository) baseRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        addressService.assignAddressToUser(user);
        User saved = saveNewUser(user);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto updateUser(UserDto userDto) {
        User foundUser = getBaseRepository().findByLogin(userDto.getLogin())
                .stream().findFirst().orElseThrow(() -> new EntityNotFoundException("User not found by login: " + userDto.getLogin()));
        updateAddressForUser(foundUser, userDto.getAddress());
        UserMapper.update(userDto, foundUser);
        return UserMapper.toDto(foundUser);
    }

    @Override
    public List<UserDto> getUsersByCountryNames(List<String> countryNames) {
        List<User> users = usersDao.getByCountryNamesInAddress(countryNames);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<UserDto>> getUsersMapByCountryNames(List<String> countryNames) {
        List<User> users = usersDao.getByCountryNamesInAddress(countryNames);
        Map<String, List<UserDto>> map = new HashMap<>();
        for (User user : users) {
            String country = user.getAddress() != null ? user.getAddress().getCountry() : null;
            if (country != null && countryNames.contains(country)) {
                populateMapOfUsersLists(map, user, country);
            }
        }
        return map;
    }

    @Override
    public List<UserDto> getUsersByCityNames(List<String> cityNames) {
        List<User> users = usersDao.getByCityNamesInAddress(cityNames);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<UserDto>> getUsersMapByCityNames(List<String> cityNames) {
        List<User> users = usersDao.getByCityNamesInAddress(cityNames);
        Map<String, List<UserDto>> map = new HashMap<>();
        for (User user : users) {
            String city = user.getAddress() != null ? user.getAddress().getCity() : null;
            if (city != null && cityNames.contains(city)) {
                populateMapOfUsersLists(map, user, city);
            }
        }
        return map;
    }

    @Override
    public List<UserDto> getUsersByLastNames(List<String> lastNames) {
        List<User> users = usersDao.getByLastNamesInUser(lastNames);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByEmailContent(String content) {
        List<User> users = usersDao.findByEmailContentInUser(content);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByEmails(List<String> emails) {
        List<User> users = usersDao.findByEmailsInUser(emails);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void assignUserToOrderByLogin(Order order, String userLogin) {
        User foundUser = getBaseRepository().findByLogin(userLogin)
                .stream().findFirst().orElseThrow(() -> new EntityNotFoundException("User not found by user login: " + userLogin));
        order.setUser(foundUser);
    }

    private void populateMapOfUsersLists(Map<String, List<UserDto>> map, User user, String key) {
        UserDto dto = UserMapper.toDto(user);
        MapUtils.populateMapOfListsByStringKey(map, dto, key);
    }

    private void updateAddressForUser(User user, AddressDto addressDto) {
        Address updatedAddress = user.getAddress();
        if (updatedAddress != null) {
            AddressMapper.update(addressDto, updatedAddress);
        } else {
            throw new EntityNotFoundException("No Address found in User entity! " + user);
        }
    }

    private User saveNewUser(User user) {
        try {
            return saveNewEntity(user);
        } catch (SQLException e) {
            log.error("User save failed! {} ", e.getMessage());
        }
        return null;
    }

}
