package com.demo.induction.tp.mock;

import com.demo.induction.tp.constants.TransactionTypeConstant;
import com.demo.induction.tp.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionMock {

    public static List<Transaction> mockTransactionList() {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(mockTransaction());
        return transactionList;
    }

    public static Transaction mockTransaction() {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionTypeConstant.CREDIT.getName());
        transaction.setAmount(BigDecimal.valueOf(25000.00));
        transaction.setNarration("Salary");
        return transaction;
    }

    public static List<Transaction> mockTransactionList(String type, String amount, String narration) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        if (!amount.isEmpty()) {
            transaction.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));
        } else {
            transaction.setAmount(null);
        }
        transaction.setNarration(narration);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        return transactionList;
    }
}
