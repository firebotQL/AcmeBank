package com.tide.domain;

import com.tide.exceptions.NotEnoughFundsException;
import com.tide.views.AccountView;
import com.tide.helper.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.PriorityQueue;

@Data
public class Account extends Entity {
    private String name;
    private String sortCode;
    private Long number;
    protected String accountHolder;
    protected BigDecimal balance;
    protected PriorityQueue<Transaction> associatedTransactions;
    private AccountView accountView;

    public Account() {
        balance = new BigDecimal(0);
        associatedTransactions = new PriorityQueue<Transaction>(10, new Comparator<Transaction>() {
            public int compare(Transaction transaction1, Transaction transaction2) {
                return transaction1.getDateTime().isBefore(transaction2.getDateTime()) ? 1 : -1;
            }
        });
        accountView = new AccountView(this);
    }

    // NOTE: Might not be the right place to subtract balance as transactions
    // might be in active state and not complete...
    public void addTransaction(Transaction transaction) throws NotEnoughFundsException {
        BigDecimal newBalance = balance.subtract(transaction.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            balance = newBalance;
            associatedTransactions.add(transaction);
            notifyUpdate();
        } else {
            throw new NotEnoughFundsException("Not enough funds to process transaction. Please add more fund or allow overdraft!");
        }

    }

    @Value
    @AllArgsConstructor
    public static class Key {
        private Long number;
        private String sortCode;
    }
}
