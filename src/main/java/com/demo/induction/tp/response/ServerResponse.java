package com.demo.induction.tp.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ServerResponse {

    private HttpStatus httpStatus;
    private String message;
    private Object data;

}
