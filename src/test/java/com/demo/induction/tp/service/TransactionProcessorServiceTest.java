package com.demo.induction.tp.service;

import com.demo.induction.tp.ApplicationTest;
import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.factory.TransactionProcessorFactory;
import com.demo.induction.tp.response.ServerResponse;
import com.demo.induction.tp.service.impl.TransactionProcessorServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

public class TransactionProcessorServiceTest extends ApplicationTest {

    @Mock
    @Qualifier(TransactionProcessorConstant.CSV)
    private TransactionProcessor csvTransactionProcessor;

    @Mock
    @Qualifier(TransactionProcessorConstant.XML)
    private TransactionProcessor xmlTransactionProcessor;

    @Mock
    private TransactionProcessorFactory transactionProcessorFactory;

    @InjectMocks
    private TransactionProcessorServiceImpl transactionProcessorService;

    @Test
    public void uploadFile_csv_shoulsReturnSuccess() throws IOException {
        Mockito.when(transactionProcessorFactory.getTransactionProcessor(TransactionProcessorConstant.CSV_FORMAT)).thenReturn(csvTransactionProcessor);
        Resource classPathResource = new ClassPathResource("csvTransaction.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", classPathResource.getFilename(),
                TransactionProcessorConstant.CSV_FORMAT, classPathResource.getInputStream());
        ServerResponse serverResponse = transactionProcessorService.uploadFile(mockMultipartFile);
        Assert.assertEquals(HttpStatus.OK, serverResponse.getHttpStatus());
    }

    @Test
    public void uploadFile_xml_shoulsReturnSuccess() throws IOException {
        Mockito.when(transactionProcessorFactory.getTransactionProcessor(TransactionProcessorConstant.XML_FORMAT)).thenReturn(xmlTransactionProcessor);
        Resource classPathResource = new ClassPathResource("xmlTransaction.xml");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", classPathResource.getFilename(),
                TransactionProcessorConstant.XML_FORMAT, classPathResource.getInputStream());
        ServerResponse serverResponse = transactionProcessorService.uploadFile(mockMultipartFile);
        Assert.assertEquals(HttpStatus.OK, serverResponse.getHttpStatus());
    }

}
