package com.demo.induction.tp.service.impl;

import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.constants.TransactionTypeConstant;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.model.Violation;
import com.demo.induction.tp.service.TransactionProcessor;
import com.demo.induction.tp.service.abstractService.AbstractTransactionProcessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@Qualifier(TransactionProcessorConstant.XML)
public class XmlTransactionProcessorImpl extends AbstractTransactionProcessor implements TransactionProcessor {

    private List<Transaction> transactionList;
    private Double creditTransactionSum;
    private Double debitTransactionSum;

    @Override
    public void importTransactions(InputStream is) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            this.transactionList = xmlMapper.readValue(is, new TypeReference<List<Transaction>>() {
            });
            log.info("XML transaction list : " + transactionList);
        } catch (IOException e) {
            log.info("Exception : " + e.getMessage());
            throw new RuntimeException("Fail to parse XML file: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getImportedTransactions() {
        this.creditTransactionSum = 0.00;
        this.debitTransactionSum = 0.00;
        transactionList.forEach(transaction -> {
            String xmlType = transaction.getType();
            if (!xmlType.isEmpty()) {
                if (xmlType.equalsIgnoreCase(TransactionTypeConstant.CREDIT.getName())) {
                    this.creditTransactionSum = creditTransactionSum + transaction.getAmount().doubleValue();
                }
                if (xmlType.equalsIgnoreCase(TransactionTypeConstant.DEBIT.getName())) {
                    this.debitTransactionSum = debitTransactionSum + transaction.getAmount().doubleValue();
                }
            }
        });
        return transactionList;
    }

    @Override
    public List<Violation> validate() {
        List<Violation> violationList = new ArrayList<>();
        int id = 0;
        for (Transaction transaction : transactionList) {
            id++;
            String transactionAmount = transaction.getAmount() != null ? transaction.getAmount().toPlainString() : "";
            violationList = validate(violationList, id, transaction.getType(), transactionAmount);
        }
        return violationList;
    }

    @Override
    public boolean isBalanced() {
        return isBalanced(creditTransactionSum, debitTransactionSum);
    }
}
