package com.tide;

import com.tide.domain.Account;
import com.tide.domain.Bank;
import com.tide.domain.Transaction;
import com.tide.domain.enums.TRANSACTION_FLAG;
import com.tide.domain.enums.TRANSACTION_TYPE;
import com.tide.exceptions.BankProcessingException;
import com.tide.exceptions.TransactionRequestException;

import java.math.BigDecimal;

public class TransactionRequest {
    private Transaction transaction;
    private Long accountNumber;
    private String sortCode;

    private boolean built = false;

    public TransactionRequest() {
        transaction = new Transaction();
    }

    public TransactionRequest withAmount(BigDecimal amount) {
        transaction.setAmount(amount);
        return this;
    }

    public TransactionRequest withTransactionFlag(TRANSACTION_FLAG transactionFlag) {
        transaction.setTransactionFlag(transactionFlag);
        return this;
    }

    public TransactionRequest withTransactionType(TRANSACTION_TYPE transactionType) {
        transaction.setTransactionType(transactionType);
        return this;
    }

    public TransactionRequest withAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public TransactionRequest withSortCode(String sortCode) {
        this.sortCode = sortCode;
        return this;
    }

    public synchronized void make() throws TransactionRequestException {
        if (built) {
            throw new TransactionRequestException("Transaction Request already used to build. Please create new Transaction Request.");
        }

        if (transaction.getAmount() == null) {
            throw new TransactionRequestException("Amount cannot be empty. Please provide amount.");
        }

        if (transaction.getTransactionFlag() == null) {
            throw new TransactionRequestException("Transaction flag cannot be empty. Please provide transaction flag.");
        }

        if (transaction.getTransactionType() == null) {
            throw new TransactionRequestException("Transaction type cannot be empty. Please provide transaction.");
        }

        if (accountNumber == null) {
            throw new TransactionRequestException("Account number cannot be empty. Please provide account number.");
        }

        if (sortCode == null) {
            throw new TransactionRequestException("Sort code cannot be empty. Please provide sort code.");
        }

        built = true;

        Account.Key key = new Account.Key(accountNumber, sortCode);
        transaction.setAssociatedAccount(key);

        try {
            Bank.getInstance().processTransaction(transaction);
        } catch (BankProcessingException e) {
            throw new TransactionRequestException(e.getMessage());
        }
    }
}
