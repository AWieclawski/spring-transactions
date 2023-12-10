package edu.awieclawski.services;

import edu.awieclawski.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class InnerService {
    @Transactional(propagation = Propagation.REQUIRED)
    public void doRequired(User user) {
        affectUser(user);
        logTransaction("inside doRequired of InnerService");
        throw new RuntimeException("Rollback this 'doRequired' transaction!");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doRequiresNew(User user) {
        affectUser(user);
        logTransaction("inside doRequiresNew of InnerService");
        throw new RuntimeException("Rollback this 'doRequiresNew' transaction!");
    }

    @Transactional(propagation = Propagation.NESTED)
    public void doNested(User user) {
        affectUser(user);
        logTransaction("inside doNested of InnerService");
        throw new RuntimeException("Rollback this 'doNested' transaction!");
    }

    private void affectUser(User user) {
        user.setFirstName(user.getFirstName() + "_affected!");
        user.setLastName(user.getLastName() + "_affected!");
    }

    private void logTransaction(String msg) {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info(" - - Transaction name: [{}] | {}", transactionName, msg);
    }
}
