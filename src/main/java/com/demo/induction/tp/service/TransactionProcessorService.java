package com.demo.induction.tp.service;

import com.demo.induction.tp.response.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface TransactionProcessorService {

    ServerResponse uploadFile(MultipartFile file);

}
