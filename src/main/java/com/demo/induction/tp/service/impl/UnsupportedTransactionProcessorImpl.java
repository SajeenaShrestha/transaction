package com.demo.induction.tp.service.impl;

import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.model.Violation;
import com.demo.induction.tp.service.TransactionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@Qualifier(TransactionProcessorConstant.UNSUPPORTED)
public class UnsupportedTransactionProcessorImpl implements TransactionProcessor {

    @Override
    public void importTransactions(InputStream is) {
        throw new RuntimeException("Unsupported file format");
    }

    @Override
    public List<Transaction> getImportedTransactions() {
        return null;
    }

    @Override
    public List<Violation> validate() {
        return null;
    }

    @Override
    public boolean isBalanced() {
        return false;
    }
}
