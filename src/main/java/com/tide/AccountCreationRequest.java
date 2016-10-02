package com.tide;

import com.tide.domain.Account;
import com.tide.domain.Bank;
import com.tide.exceptions.AccountCreationRequestException;

import java.math.BigDecimal;

public class AccountCreationRequest {

    private Account account;
    private boolean opened = false;

    public AccountCreationRequest() {
        account = new Account();
    }

    public AccountCreationRequest withName(String name) {
        account.setName(name);
        return this;
    }

    public AccountCreationRequest withAccountHolder(String accountHolder) {
        account.setAccountHolder(accountHolder);
        return this;
    }

    public AccountCreationRequest withInitialBalance(BigDecimal balance) {
        account.setBalance(balance);
        return this;
    }

    public synchronized void open() throws AccountCreationRequestException {
        if (opened) {
            throw new AccountCreationRequestException("Account already opened with this request. Please try creating new request.");
        }

        if (account.getName() == null) {
            throw new AccountCreationRequestException("Please provide an account name.");
        }

        if (account.getAccountHolder() == null) {
            throw new AccountCreationRequestException("Please provide account holder.");
        }

        opened = true;
        Bank.getInstance().openNewAccount(account);
    }
}
