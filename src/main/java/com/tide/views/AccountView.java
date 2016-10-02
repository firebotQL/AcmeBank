package com.tide.views;

import com.tide.helper.Observer;
import com.tide.domain.Account;
import com.tide.domain.Transaction;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
public class AccountView extends Observer {
    private String accountHolder;
    private BigDecimal balance;
    private List<TransactionView> transactions;
    private Queue<TransactionView> lastTenTransactions;

    public AccountView(Account account) {
        this.entity = account;
        this.entity.attach(this);
        this.transactions = new ArrayList();
        this.lastTenTransactions = new LinkedList();
        update();
    }

    @Override
    public void update() {
        Account account = (Account)entity;
        this.accountHolder = account.getAccountHolder();
        this.balance = new BigDecimal(0).add(account.getBalance());
        Transaction lastTransaction = account.getAssociatedTransactions().peek();
        if (lastTransaction != null) {
            TransactionView transactionView = new TransactionView(lastTransaction);

            transactions.add(transactionView);
            if (lastTenTransactions.size() == 10) {
                lastTenTransactions.remove();
            }
            lastTenTransactions.offer(transactionView);
        }
    }
}
