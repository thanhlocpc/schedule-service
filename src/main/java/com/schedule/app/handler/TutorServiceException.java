package com.schedule.app.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/
public class TutorServiceException extends ResponseStatusException {

    @Getter
    private Object data;

    public TutorServiceException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public TutorServiceException(String message, Object data) {
        super(HttpStatus.NOT_FOUND, message);
        this.data = data;
    }

}
