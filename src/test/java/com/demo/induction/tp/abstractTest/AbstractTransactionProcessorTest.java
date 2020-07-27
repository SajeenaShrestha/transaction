package com.demo.induction.tp.abstractTest;

import com.demo.induction.tp.ApplicationTest;
import com.demo.induction.tp.constants.TransactionMessageConstant;
import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.model.Violation;
import com.demo.induction.tp.service.TransactionProcessor;
import org.junit.Assert;

import java.util.List;

public abstract class AbstractTransactionProcessorTest extends ApplicationTest {

    protected void test_missingTransactionType(TransactionProcessor transactionProcessor) {
        List<Violation> violationList = transactionProcessor.validate();
        Assert.assertFalse(violationList.isEmpty());
        violationList.forEach(violation -> {
            Assert.assertEquals(violation.getProperty(), TransactionProcessorConstant.TYPE);
            Assert.assertEquals(violation.getDescription(), TransactionMessageConstant.MISSING_TRANSACTION_TYPE);
        });
    }

    protected void test_invalidTransactionType(TransactionProcessor transactionProcessor) {
        List<Violation> violationList = transactionProcessor.validate();
        Assert.assertFalse(violationList.isEmpty());
        violationList.forEach(violation -> {
            Assert.assertEquals(violation.getProperty(), TransactionProcessorConstant.TYPE);
            Assert.assertEquals(violation.getDescription(), TransactionMessageConstant.INVALID_TRANSACTION_TYPE);
        });
    }

    protected void test_missingTransactionAmount(TransactionProcessor transactionProcessor) {
        List<Violation> violationList = transactionProcessor.validate();
        Assert.assertFalse(violationList.isEmpty());
        violationList.forEach(violation -> {
            Assert.assertEquals(violation.getProperty(), TransactionProcessorConstant.AMOUNT);
            Assert.assertEquals(violation.getDescription(), TransactionMessageConstant.MISSING_TRANSACTION_AMOUNT);
        });
    }

    protected void test_greaterThanZero(TransactionProcessor transactionProcessor) {
        List<Violation> violationList = transactionProcessor.validate();
        Assert.assertFalse(violationList.isEmpty());
        violationList.forEach(violation -> {
            Assert.assertEquals(violation.getProperty(), TransactionProcessorConstant.AMOUNT);
            Assert.assertEquals(violation.getDescription(), TransactionMessageConstant.TRANSACTION_AMOUNT_SHOULD_BE_GREATER_THAN_ZERO);
        });
    }

}
