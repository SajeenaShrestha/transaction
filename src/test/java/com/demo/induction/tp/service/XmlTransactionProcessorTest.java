package com.demo.induction.tp.service;

import com.demo.induction.tp.abstractTest.AbstractTransactionProcessorTest;
import com.demo.induction.tp.mock.TransactionMock;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.service.impl.XmlTransactionProcessorImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlTransactionProcessorTest extends AbstractTransactionProcessorTest {

    @InjectMocks
    private XmlTransactionProcessorImpl transactionProcessor;

    @Test
    public void importTransactions_shouldBeSuccess() throws IOException {
        Resource classPathResource = new ClassPathResource("xmlTransaction.xml");
        transactionProcessor.importTransactions(classPathResource.getInputStream());
        Assert.assertFalse(transactionProcessor.getTransactionList().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void importTransactions_emptyCsvRecords() throws IOException {
        Resource classPathResource = new ClassPathResource("emptyXmlTransaction.xml");
        transactionProcessor.importTransactions(classPathResource.getInputStream());
    }

    @Test
    public void getImportedTransactions_shouldReturn_transactionList() throws IOException {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList());
        List<Transaction> transactionList = transactionProcessor.getImportedTransactions();
        Assert.assertFalse(transactionList.isEmpty());
    }

    @Test
    public void getImportedTransactions_shouldReturn_emptyList() throws IOException {
        transactionProcessor.setTransactionList(new ArrayList<>());
        List<Transaction> transactionList = transactionProcessor.getImportedTransactions();
        Assert.assertTrue(transactionList.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void getImportedTransactions_shouldThrow_nullPointerException() throws IOException {
        transactionProcessor.getImportedTransactions();
    }

    @Test
    public void validate_shouldReturn_emptyList() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("D", "61.22", "Electricity bill"));
        Assert.assertTrue(transactionProcessor.validate().isEmpty());
    }

    @Test
    public void validate_shouldReturn_nonEmptyList() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("", "", ""));
        Assert.assertFalse(transactionProcessor.validate().isEmpty());
    }

    @Test
    public void validate_shouldReturn_missingTransactionType() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("", "500.00", "Electricity bill"));
        test_missingTransactionType(transactionProcessor);
    }

    @Test
    public void validate_shouldReturn_invalidTransactionType() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("A", "500.00", "Electricity bill"));
        test_invalidTransactionType(transactionProcessor);
    }

    @Test
    public void validate_shouldReturn_missingTransactionAmount() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("C", "", "Electricity bill"));
        test_missingTransactionAmount(transactionProcessor);
    }

    @Test
    public void validate_shouldReturn_greaterThanZero() throws Exception {
        transactionProcessor.setTransactionList(TransactionMock.mockTransactionList("D", "0.00", "Electricity bill"));
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
