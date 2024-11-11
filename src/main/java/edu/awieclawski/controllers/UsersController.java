package edu.awieclawski.controllers;

import edu.awieclawski.dtos.ContentDto;
import edu.awieclawski.dtos.UserDto;
import edu.awieclawski.entities.User;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.exceptions.RestException;
import edu.awieclawski.services.ExternalService;
import edu.awieclawski.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users/")
public class UsersController {

    private final ExternalService externalService;
    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {
        checkObject(id);
        Optional<User> user = externalService.getById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("No User with id=[" + id + "]");
        }
    }

    @PostMapping(path = "bycountrynames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> byCountryNames(@RequestBody List<String> countryNames) {
        checkObject(countryNames);
        List<UserDto> users = userService.getUsersByCountryNames(countryNames);
        return handleResultsList(users, "No User with countries: " + countryNames);
    }

    @PostMapping(path = "map/bycountrynames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<UserDto>>> byCountryNamesMap(@RequestBody List<String> countryNames) {
        checkObject(countryNames);
        Map<String, List<UserDto>> usersMap = userService.getUsersMapByCountryNames(countryNames);
        return handleResultsMap(usersMap, "No User with countries: " + countryNames);
    }

    @PostMapping(path = "bycitynames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> byCityNames(@RequestBody List<String> cityNames) {
        checkObject(cityNames);
        List<UserDto> users = userService.getUsersByCityNames(cityNames);
        return handleResultsList(users, "No User with cities: " + cityNames);
    }

    @PostMapping(path = "map/bycitynames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<UserDto>>> byCityNamesMap(@RequestBody List<String> cityNames) {
        checkObject(cityNames);
        Map<String, List<UserDto>> usersMap = userService.getUsersMapByCityNames(cityNames);
        return handleResultsMap(usersMap, "No User with cities: " + cityNames);
    }

    @PostMapping(path = "bylastnames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> byLastNames(@RequestBody List<String> lastNames) {
        checkObject(lastNames);
        List<UserDto> users = userService.getUsersByLastNames(lastNames);
        return handleResultsList(users, "No User with names: " + lastNames);
    }

    @PostMapping(path = "byemailcontent",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> byEmailContent(@RequestBody ContentDto content) {
        checkObject(content);
        List<UserDto> users = userService.getUsersByEmailContent(content.getContent());
        return handleResultsList(users, "No User with email containing: " + content.getContent());
    }

    @PostMapping(path = "byemails",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> byEmails(@RequestBody List<String> emails) {
        checkObject(emails);
        List<UserDto> users = userService.getUsersByEmails(emails);
        return handleResultsList(users, "No User with emails in: " + emails);
    }

    @PostMapping(path = "save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> saveUser(@RequestBody UserDto newUser) {
        checkObject(newUser);
        UserDto userDto = userService.saveUser(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> updateUser(@RequestBody UserDto updatedUser) {
        checkObject(updatedUser);
        UserDto userDto = userService.updateUser(updatedUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "required-required",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> requiredRequired(@RequestBody UserDto newUser) {
        checkObject(newUser);
        UserDto userDto = externalService.requiredRequired(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "required-requiresnew",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> requiredRequiresNew(@RequestBody UserDto newUser) {
        checkObject(newUser);
        UserDto userDto = externalService.requiredRequiresNew(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "required-nested",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> requiredNested(@RequestBody UserDto newUser) {
        checkObject(newUser);
        UserDto userDto = externalService.requiredNested(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    private void checkObject(Object object) {
        if (object == null) {
            throw new RestException("No entity in the body!");
        }
    }

    private ResponseEntity<List<UserDto>> handleResultsList(List<UserDto> userDtoList, String errMsg) {
        if (CollectionUtils.isNotEmpty(userDtoList)) {
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }

    private ResponseEntity<Map<String, List<UserDto>>> handleResultsMap(Map<String, List<UserDto>> usersMap, String errMsg) {
        if (MapUtils.isNotEmpty(usersMap)) {
            return new ResponseEntity<>(usersMap, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(errMsg);
        }
    }
}