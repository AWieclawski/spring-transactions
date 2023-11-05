package edu.awieclawski.controllers;

import edu.awieclawski.entities.User;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.exceptions.RestException;
import edu.awieclawski.services.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "users/")
public class UsersController {
    private final RestTemplate restTemplate = new RestTemplate();

    private final ExternalService service;

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {
        checkObject(id);
        Optional<User> user = service.getById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("No User with id=[" + id+"]");
        }
    }

    @PostMapping(path = "required-required",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> requiredRequired(@RequestBody User newUser) {
        checkObject(newUser);
        User user = service.requiredRequired(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(path = "required-requiresnew",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> requiredRequiresNew(@RequestBody User newUser) {
        checkObject(newUser);
        User user = service.requiredRequiresNew(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(path = "required-nested",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> requiredNested(@RequestBody User newUser) {
        checkObject(newUser);
        User user = service.requiredNested(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private void checkObject(Object object) {
        if (object == null) {
            throw new RestException("No entity in the body!");
        }
    }
}