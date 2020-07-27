package com.demo.induction.tp.Utility;

import com.demo.induction.tp.response.ServerResponse;
import org.springframework.http.HttpStatus;

public class ResponseUtility {

    public static ServerResponse getSuccessfulServerResponse(String message, Object data) {
        return ServerResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    public static ServerResponse getFailedServerResponse(HttpStatus httpStatus, String message) {
        return ServerResponse.builder()
                .httpStatus(httpStatus)
                .message(message)
                .build();
    }

}
