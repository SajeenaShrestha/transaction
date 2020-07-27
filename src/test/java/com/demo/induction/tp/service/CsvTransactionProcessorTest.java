package com.demo.induction.tp.service;

import com.demo.induction.tp.abstractTest.AbstractTransactionProcessorTest;
import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.service.impl.CsvTransactionProcessorImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvTransactionProcessorTest extends AbstractTransactionProcessorTest {

    private CSVRecord csvRecordWithHeader;
    private String[] values;

    @InjectMocks
    private CsvTransactionProcessorImpl transactionProcessor;

    @Before
    public void setUp() throws Exception {
        getCsvRecord("C", "500.00", "Electricity Bill");
    }

    public CSVRecord getCsvRecord(String type, String amount, String narration) throws Exception {
        values = new String[]{type, amount, narration};
        final String rowData = StringUtils.join(values, ',');
        final String[] headers = {TransactionProcessorConstant.TYPE, TransactionProcessorConstant.AMOUNT, TransactionProcessorConstant.NARRATION};
        try (final CSVParser parser = CSVFormat.DEFAULT.withHeader(headers).parse(new StringReader(rowData))) {
            csvRecordWithHeader = parser.iterator().next();
        }
        return csvRecordWithHeader;
    }

    public List<CSVRecord> getCSVRecordList(String type, String amount, String narration) {
        List<CSVRecord> csvRecordList = new ArrayList<>();
        try {
            CSVRecord csvRecord = getCsvRecord(type, amount, narration);
            csvRecordList.add(csvRecord);
            CSVRecord secondCsvRecord = getCsvRecord("C", "500.00", "Social security payment");
            csvRecordList.add(secondCsvRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return csvRecordList;
    }

    @Test
    public void test_recordWithHeader() {
        Assert.assertEquals(values[0], csvRecordWithHeader.get(TransactionProcessorConstant.TYPE));
        Assert.assertEquals(values[1], csvRecordWithHeader.get(TransactionProcessorConstant.AMOUNT));
        Assert.assertEquals(values[2], csvRecordWithHeader.get(TransactionProcessorConstant.NARRATION));
    }

    @Test
    public void importTransactions_shouldBeSuccess() throws IOException {
        Resource classPathResource = new ClassPathResource("csvTransaction.csv");
        transactionProcessor.importTransactions(classPathResource.getInputStream());
        Assert.assertFalse(transactionProcessor.getCsvRecords().isEmpty());
    }

    @Test
    public void importTransactions_emptyCsvRecords() throws IOException {
        Resource classPathResource = new ClassPathResource("emptyCsvTransaction.csv");
        transactionProcessor.importTransactions(classPathResource.getInputStream());
        Assert.assertTrue(transactionProcessor.getCsvRecords().isEmpty());
    }

    @Test
    public void getImportedTransactions_shouldReturn_transactionList() throws IOException {
        transactionProcessor.setCsvRecords(getCSVRecordList("D", "61.22", "Electricity bill"));
        List<Transaction> transactionList = transactionProcessor.getImportedTransactions();
        Assert.assertFalse(transactionList.isEmpty());
    }

    @Test
    public void getImportedTransactions_shouldReturn_emptyList() throws IOException {
        transactionProcessor.setCsvRecords(new ArrayList<>());
        List<Transaction> transactionList = transactionProcessor.getImportedTransactions();
        Assert.assertTrue(transactionList.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void getImportedTransactions_shouldThrow_nullPointerException() throws IOException {
        transactionProcessor.getImportedTransactions();
    }

    @Test
    public void validate_shouldreturn_emptyList() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("D", "61.22", "Electricity bill"));
        Assert.assertTrue(transactionProcessor.validate().isEmpty());
    }

    @Test
    public void validate_shouldreturn_nonEmptyList() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("", "", ""));
        Assert.assertFalse(transactionProcessor.validate().isEmpty());
    }

    @Test
    public void validate_shouldreturn_missingTransactionType() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("", "500.00", "Electricity bill"));
        test_missingTransactionType(transactionProcessor);
    }

    @Test
    public void validate_shouldreturn_invalidTransactionType() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("A", "500.00", "Electricity bill"));
        test_invalidTransactionType(transactionProcessor);
    }

    @Test
    public void validate_shouldreturn_missingTransactionAmount() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("C", "", "Electricity bill"));
        test_missingTransactionAmount(transactionProcessor);
    }

    @Test
    public void validate_shouldreturn_greaterThanZero() throws Exception {
        transactionProcessor.setCsvRecords(getCSVRecordList("D", "0.00", "Electricity bill"));
        test_greaterThanZero(transactionProcessor);
    }

    @Test
    public void isBalanced_shouldReturn_true() {
        transactionProcessor.setCreditTransactionSum(500.00);
        transactionProcessor.setDebitTransactionSum(500.00);
        Assert.assertTrue(transactionProcessor.isBalanced());
    }

    @Test
    public void isBalanced_shouldReturn_false() {
        transactionProcessor.setCreditTransactionSum(500.00);
        transactionProcessor.setDebitTransactionSum(1000.00);
        Assert.assertFalse(transactionProcessor.isBalanced());
    }

}
