package com.demo.induction.tp.service.abstractService;

import com.demo.induction.tp.constants.TransactionMessageConstant;
import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.constants.TransactionTypeConstant;
import com.demo.induction.tp.model.Violation;

import java.util.List;

public abstract class AbstractTransactionProcessor {

    protected List<Violation> validate(List<Violation> violationList, int id, String recordType, String recordAmount) {
        if (recordType.isEmpty()) {
            violationList.add(setViolation(id, TransactionProcessorConstant.TYPE, TransactionMessageConstant.MISSING_TRANSACTION_TYPE));
        }
        if (!recordType.isEmpty() && !(recordType.equalsIgnoreCase(TransactionTypeConstant.CREDIT.getName())
                || recordType.equalsIgnoreCase(TransactionTypeConstant.DEBIT.getName()))) {
            violationList.add(setViolation(id, TransactionProcessorConstant.TYPE, TransactionMessageConstant.INVALID_TRANSACTION_TYPE));
        }
        if (recordAmount.isEmpty()) {
            violationList.add(setViolation(id, TransactionProcessorConstant.AMOUNT, TransactionMessageConstant.MISSING_TRANSACTION_AMOUNT));
        }
        if (!recordAmount.isEmpty() && Double.valueOf(recordAmount) <= 0.00) {
            violationList.add(setViolation(id, TransactionProcessorConstant.AMOUNT, TransactionMessageConstant.TRANSACTION_AMOUNT_SHOULD_BE_GREATER_THAN_ZERO));
        }
        return violationList;
    }

    private Violation setViolation(int order, String property, String description) {
        Violation violation = new Violation();
        violation.setOrder(order);
        violation.setProperty(property);
        violation.setDescription(description);
        return violation;
    }

    protected boolean isBalanced(Double creditTransactionSum, Double debitTransactionSum) {
        return creditTransactionSum.equals(debitTransactionSum);
    }

}
