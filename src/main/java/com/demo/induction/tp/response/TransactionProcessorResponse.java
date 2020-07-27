package com.demo.induction.tp.response;

import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.model.Violation;
import lombok.Data;

import java.util.List;

@Data
public class TransactionProcessorResponse {

    private String fileName;
    private List<Violation> violationList;
    private Boolean isBalanced;
    private List<Transaction> transactionList;
}
