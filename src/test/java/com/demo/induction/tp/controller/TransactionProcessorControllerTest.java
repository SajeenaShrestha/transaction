package com.demo.induction.tp.controller;

import com.demo.induction.tp.ApplicationTest;
import com.demo.induction.tp.Utility.ResponseUtility;
import com.demo.induction.tp.constants.ApiConstant;
import com.demo.induction.tp.constants.TransactionProcessorConstant;
import com.demo.induction.tp.mock.ResponseMock;
import com.demo.induction.tp.response.ServerResponse;
import com.demo.induction.tp.service.TransactionProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionProcessorControllerTest extends ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionProcessorService transactionProcessorService;

    @InjectMocks
    private TransactionProcessorController transactionProcessorController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionProcessorController).build();
    }

    @Test
    public void uploadFile_csv_shouldReturnSuccess() throws Exception {
        Resource classPathResource = new ClassPathResource("csvTransaction.csv");
        test_uploadFile(classPathResource);
    }

    @Test
    public void uploadFile_xml_shouldReturnSuccess() throws Exception {
        Resource classPathResource = new ClassPathResource("xmlTransaction.xml");
        test_uploadFile(classPathResource);
    }

    private void test_uploadFile(Resource classPathResource) throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", classPathResource.getFilename(),
                TransactionProcessorConstant.CSV_FORMAT, classPathResource.getInputStream());
        ServerResponse serverResponse = ResponseUtility.getSuccessfulServerResponse("Uploaded the file successfully ", ResponseMock.mockTransactionProcessorResponse());
        Mockito.when(transactionProcessorService.uploadFile(ArgumentMatchers.any())).thenReturn(serverResponse);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/" + ApiConstant.API + ApiConstant.TRANSACTION + "/" + ApiConstant.UPLOAD)
                        .file(mockMultipartFile)
                        .content(mockMultipartFile.getBytes())
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(TransactionProcessorConstant.CSV_FORMAT);

        this.mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
