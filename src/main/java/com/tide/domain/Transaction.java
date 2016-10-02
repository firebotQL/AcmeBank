package com.tide.domain;

import com.tide.domain.enums.STATE;
import com.tide.domain.enums.TRANSACTION_FLAG;
import com.tide.domain.enums.TRANSACTION_TYPE;
import com.tide.helper.Entity;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Transaction extends Entity {
    protected final String identifier;
    private Account.Key associatedAccount;
    protected BigDecimal amount;
    protected TRANSACTION_FLAG transactionFlag;
    protected DateTime dateTime;
    protected STATE state;
    protected TRANSACTION_TYPE transactionType;

    public Transaction() {
        identifier = UUID.randomUUID().toString();
        state = STATE.ACTIVE;
    }

    public void changeState(STATE state) {
        this.state = state;
        notifyUpdate();
    }
}
