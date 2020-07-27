package com.demo.induction.tp.mock;

import com.demo.induction.tp.response.TransactionProcessorResponse;

import java.util.ArrayList;

public class ResponseMock {

    public static TransactionProcessorResponse mockTransactionProcessorResponse() {
        TransactionProcessorResponse response = new TransactionProcessorResponse();
        response.setFileName("transaction");
        response.setViolationList(new ArrayList<>());
        response.setIsBalanced(false);
        response.setTransactionList(TransactionMock.mockTransactionList());
        return response;
    }
}
