package com.tide.views;

import com.tide.domain.Transaction;
import com.tide.domain.enums.STATE;
import com.tide.domain.enums.TRANSACTION_FLAG;
import com.tide.helper.Observer;
import org.joda.time.DateTime;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionView extends Observer {
    private BigDecimal amount;
    private TRANSACTION_FLAG transactionFlag;
    private DateTime dateTime;
    private STATE state;

    public TransactionView(Transaction transaction) {
        this.entity = transaction;
        this.entity.attach(this);
        update();
    }

    @Override
    public void update() {
        Transaction transaction = (Transaction)entity;
        setAmount(transaction.getAmount());
        setTransactionFlag(transaction.getTransactionFlag());
        setDateTime(transaction.getDateTime());
        setState(transaction.getState());
    }
}
