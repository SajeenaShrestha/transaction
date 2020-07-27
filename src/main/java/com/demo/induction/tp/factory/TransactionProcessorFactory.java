package com.demo.induction.tp.factory;

import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessorFactory {

    @Autowired
    @Qualifier(TransactionProcessorConstant.CSV)
    private TransactionProcessor csvTransactionProcessor;

    @Autowired
    @Qualifier(TransactionProcessorConstant.XML)
    private TransactionProcessor xmlTransactionProcessor;

    @Autowired
    @Qualifier(TransactionProcessorConstant.UNSUPPORTED)
    private TransactionProcessor unsupported;

    public TransactionProcessor getTransactionProcessor(String fileType) {
        TransactionProcessor transactionProcessor = unsupported;
        if (TransactionProcessorConstant.CSV_FORMAT.equals(fileType)) {
            transactionProcessor = csvTransactionProcessor;
        } else if (TransactionProcessorConstant.XML_FORMAT.equals(fileType)) {
            transactionProcessor = xmlTransactionProcessor;
        }
        return transactionProcessor;
    }
}
