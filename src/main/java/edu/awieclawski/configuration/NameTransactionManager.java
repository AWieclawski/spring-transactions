package edu.awieclawski.configuration;

import edu.awieclawski.utils.InstantUtils;
import lombok.NonNull;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class NameTransactionManager
        extends org.springframework.orm.jpa.JpaTransactionManager {


    public static final String PATTERN_FORMAT = "yyyyMMddHHmmssSSSSSSSS";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
            .withZone(ZoneId.systemDefault());

    @Override
    protected void prepareSynchronization(DefaultTransactionStatus status, @NonNull TransactionDefinition definition) {
        if (status.isNewSynchronization()) {
            super.prepareSynchronization(status, definition);
            String trxName = definition.getName();
            trxName = String.format("%s_%s", trxName, InstantUtils.getCurrentTimestampAsString(DATE_TIME_FORMATTER));
            TransactionSynchronizationManager.setCurrentTransactionName(trxName);
        }
    }

}
