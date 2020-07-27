package com.demo.induction.tp.controller;

import com.demo.induction.tp.constants.ApiConstant;
import com.demo.induction.tp.response.ServerResponse;
import com.demo.induction.tp.service.TransactionProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = ApiConstant.API + ApiConstant.TRANSACTION)
public class TransactionProcessorController {
    @Autowired
    private TransactionProcessorService transactionProcessorService;

    @PostMapping(value = ApiConstant.UPLOAD)
    public ResponseEntity<ServerResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        ServerResponse response = transactionProcessorService.uploadFile(file);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
