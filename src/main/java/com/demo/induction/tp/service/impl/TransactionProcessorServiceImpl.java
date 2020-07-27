package com.demo.induction.tp.service.impl;

import com.demo.induction.tp.Utility.ResponseUtility;
import com.demo.induction.tp.factory.TransactionProcessorFactory;
import com.demo.induction.tp.model.Transaction;
import com.demo.induction.tp.model.Violation;
import com.demo.induction.tp.response.ServerResponse;
import com.demo.induction.tp.response.TransactionProcessorResponse;
import com.demo.induction.tp.service.TransactionProcessor;
import com.demo.induction.tp.service.TransactionProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class TransactionProcessorServiceImpl implements TransactionProcessorService {

    @Autowired
    private TransactionProcessorFactory transactionProcessorFactory;

    @Override
    public ServerResponse uploadFile(MultipartFile file) {
        TransactionProcessorResponse processorResponse = new TransactionProcessorResponse();
        TransactionProcessor transactionProcessor = transactionProcessorFactory.getTransactionProcessor(file.getContentType());
        try {
            transactionProcessor.importTransactions(file.getInputStream());
            processorResponse.setFileName(file.getOriginalFilename());
            List<Violation> violationList = transactionProcessor.validate();
            if (!violationList.isEmpty()) {
                processorResponse.setViolationList(violationList);
                return ResponseUtility.getSuccessfulServerResponse("Cannot upload the file : " + file.getOriginalFilename(), processorResponse);
            }
            List<Transaction> transactionList = transactionProcessor.getImportedTransactions();
            processorResponse.setTransactionList(transactionList);
            processorResponse.setIsBalanced(transactionProcessor.isBalanced());
            return ResponseUtility.getSuccessfulServerResponse("Uploaded the file successfully : " + file.getOriginalFilename(), processorResponse);
        } catch (Exception e) {
            log.info("Exception : " + e.getMessage());
            return ResponseUtility.getFailedServerResponse(HttpStatus.EXPECTATION_FAILED, "Could not upload the file : " + file.getOriginalFilename());
        }
    }

}
