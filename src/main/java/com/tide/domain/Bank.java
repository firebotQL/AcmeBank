package com.tide.domain;

import com.tide.domain.enums.STATE;
import com.tide.exceptions.NotEnoughFundsException;
import com.tide.views.AccountView;
import com.tide.exceptions.BankProcessingException;
import com.tide.views.TransactionView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {

    private Map<Account.Key, Account> map;
    private AtomicLong accountNumberGenerationHolder;
    private String sortCode;

    private static class BankLoader {
        private static final Bank BANK_INSTANCE = new Bank();
    }

    private Bank() {
        if (BankLoader.BANK_INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
        this.map = new HashMap<Account.Key, Account>();
        this.accountNumberGenerationHolder = new AtomicLong(1);
        this.sortCode = "30-55-31";     // atm this is hardcoded! :(
    }

    public static Bank getInstance() {
        return BankLoader.BANK_INSTANCE;
    }

    public void openNewAccount(Account account) {
        account.setNumber(accountNumberGenerationHolder.getAndIncrement());
        account.setSortCode(sortCode);
        map.put(new Account.Key(account.getNumber(), account.getSortCode()), account);

    }

    // 1) Provided with the details of a transaction we need to be able to add the transaction
    // to a bank account.
    public void processTransaction(Transaction transaction) throws BankProcessingException {
        Account account = map.get(transaction.getAssociatedAccount());
        if (account != null) {
            synchronized (account) {
                try {
                    account.addTransaction(transaction);
                } catch (NotEnoughFundsException exception) {
                    throw new BankProcessingException(exception.getMessage());
                }
            }
        } else {
            throw new BankProcessingException("Account doesn't exist!");
        }
    }

    // 2) Provided with the details of an account, we need to be able to retrieve
    // the current balance of a bank account
    public BigDecimal getAccountBalance(Long accountNumber, String sortCode) throws BankProcessingException {
        Account account = map.get(new Account.Key(accountNumber, sortCode));
        if (account != null) {
            synchronized (account) {
                return account.getAccountView().getBalance();
            }
        } else {
            throw new BankProcessingException("Account doesn't exist!");
        }
    }


    // 3) And display an account summary, including the last 10 transactions,
    // account balance, and account holder name.
    public AccountView getAccountView(Long accountNumber, String sortCode) throws BankProcessingException {
        Account account = map.get(new Account.Key(accountNumber, sortCode));
        if (account == null) {
            synchronized (account) {
                return account.getAccountView();
            }
        } else {
            throw new BankProcessingException("Account doesn't exist!");
        }
    }

    // 3) Method for providing a list of "active" transactions for an account, as
    // sometimes the details of active transaction need updating (e.g. transaction
    // type), It's important to note here a transaction can have one of several states
    // - active, cancelled, and complete. Any transactions that do not appear in the active
    // list must be marked as cancelled in the account, with the exception of cleared
    // transactions.
    // Method for providing a list of "cleared" transactions for an account

    // NOTE: This is naive solution. In reality don't need to iterate through all of
    // them each time. Better solution is to maintain a map/list per each state.
    public List<TransactionView> getTransactionByStateForAccount(Long accountNumber, String sortCode, STATE state) throws BankProcessingException {
        Account account = map.get(new Account.Key(accountNumber, sortCode));
        if (account == null) {
            synchronized (account) {
                List<TransactionView> transactionViewList = new ArrayList();
                for(TransactionView transactionView : account.getAccountView().getTransactions()) {
                    if (transactionView.getState() == state) {
                        transactionViewList.add(transactionView);
                    }
                }
                return transactionViewList;
            }
        } else {
            throw new BankProcessingException("Account doesn't exist!");
        }
    }
}
