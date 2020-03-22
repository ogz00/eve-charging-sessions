package com.oguz.demo.evechargingsessions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SummaryFetchingFailedException extends RuntimeException{
    private static final long serialVersionUID = 123L;

    public SummaryFetchingFailedException() {
        super();
    }

    public SummaryFetchingFailedException(String message) {
        super(message);
    }
}
