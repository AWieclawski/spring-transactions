package edu.awieclawski.services;

import edu.awieclawski.dtos.UserDto;
import edu.awieclawski.mappers.UserMapper;
import edu.awieclawski.repositories.UserRepository;
import edu.awieclawski.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ExternalService {

    private final InnerService innerService;

    private final UserRepository userRepository;

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Data saved by repository will not be persisted in the database.
     * Even though the exception is caught, the physical transaction will be rolled back due to an unchecked exception
     * from external service
     *
     * @param userDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto requiredRequired(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setTransactionName("required-required");
        user = userRepository.save(user);
        logAtStart(user);
        try {
            logTransaction("before call innerService.doRequired()");
            innerService.doRequired(user);
        } catch (RuntimeException e) {
            logTransaction("catching exception from innerService.doRequired()");
        }
        logResult(user);
        return UserMapper.toDto(user);
    }


    /**
     * The unchecked exception will roll back the internal transaction,
     * but since it is caught, it will not affect the external transaction
     *
     * @param userDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto requiredRequiresNew(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setTransactionName("required-requiresnew");
        user = userRepository.save(user);
        logAtStart(user);
        try {
            logTransaction("before call innerService.doRequiresNew()");
            innerService.doRequiresNew(user);
        } catch (RuntimeException e) {
            logTransaction("catching exception from innerService.doRequiresNew()");
        }
        logResult(user);
        return UserMapper.toDto(user);
    }

    /**
     * The unchecked exception will roll back the data of the internal method,
     * if it was not caught, it would roll back all the changes
     *
     * @param userDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto requiredNested(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setTransactionName("required-nested");
        user = userRepository.save(user);
        logAtStart(user);
        try {
            logTransaction("before call innerService.doNested()");
            innerService.doNested(user);
        } catch (RuntimeException e) {
            logTransaction("catching exception from innerService.doNested()");
        }
        logResult(user);
        return UserMapper.toDto(user);
    }

    private void logTransaction(String msg) {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info(" - Transaction name: [{}] | {}", transactionName, msg);
    }

    private void logResult(User user) {
        log.info(" >> Transaction Result: {}", user);
    }

    private void logAtStart(User user) {
        log.info(" + Entity saved: {}", user);
    }
}