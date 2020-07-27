package com.demo.induction.tp.service.impl;

import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.constants.TransactionTypeConstant;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.model.Violation;
import com.demo.induction.tp.service.TransactionProcessor;
import com.demo.induction.tp.service.abstractService.AbstractTransactionProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@Qualifier(TransactionProcessorConstant.CSV)
public class CsvTransactionProcessorImpl extends AbstractTransactionProcessor implements TransactionProcessor {

    private List<CSVRecord> csvRecords;
    private Double creditTransactionSum;
    private Double debitTransactionSum;

    @Override
    public void importTransactions(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            this.csvRecords = csvParser.getRecords();
        } catch (IOException e) {
            log.info("Exception : " + e.getMessage());
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getImportedTransactions() {
        this.creditTransactionSum = 0.00;
        this.debitTransactionSum = 0.00;
        List<Transaction> transactionList = new ArrayList<>();
        this.csvRecords.forEach(csvRecord -> {
            Transaction transaction = setTransaction(csvRecord);
            transactionList.add(transaction);
            String csvRecordType = csvRecord.get(TransactionProcessorConstant.TYPE);
            String csvRecordAmount = csvRecord.get(TransactionProcessorConstant.AMOUNT);
            if (!csvRecordType.isEmpty()) {
                if (csvRecordType.equalsIgnoreCase(TransactionTypeConstant.CREDIT.getName())) {
                    this.creditTransactionSum = creditTransactionSum + Double.parseDouble(csvRecordAmount);
                }
                if (csvRecordType.equalsIgnoreCase(TransactionTypeConstant.DEBIT.getName())) {
                    this.debitTransactionSum = debitTransactionSum + Double.parseDouble(csvRecordAmount);
                }
            }
        });
        log.info("Transaction List : " + transactionList);
        return transactionList;
    }

    @Override
    public List<Violation> validate() {
        List<Violation> violationList = new ArrayList<>();
        int id = 0;
        for (CSVRecord csvRecord : csvRecords) {
            id++;
            violationList = validate(violationList, id, csvRecord.get(TransactionProcessorConstant.TYPE), csvRecord.get(TransactionProcessorConstant.AMOUNT));
        }
        return violationList;
    }

    @Override
    public boolean isBalanced() {
        return isBalanced(creditTransactionSum, debitTransactionSum);
    }

    private Transaction setTransaction(CSVRecord record) {
        Transaction transaction = new Transaction();
        transaction.setType(record.get(TransactionProcessorConstant.TYPE));
        transaction.setAmount(BigDecimal.valueOf(Double.valueOf(record.get(TransactionProcessorConstant.AMOUNT))));
        transaction.setNarration(record.get(TransactionProcessorConstant.NARRATION));
        return transaction;
    }

}
