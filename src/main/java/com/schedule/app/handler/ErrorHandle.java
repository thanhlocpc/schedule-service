package com.schedule.app.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ErrorHandle extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = {Exception.class})
//    protected ObjectResponseWrapper handleResponseException(Exception e) {
//        return ObjectResponseWrapper.builder().status(0).message(e.getMessage()).build();
//    }

    @ExceptionHandler(value = {ScheduleServiceException.class})
    protected ObjectResponseWrapper handleResponseStatusException(ScheduleServiceException e) {
        return ObjectResponseWrapper.builder().status(0).message(e.getReason()).data(e.getData()).build();
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ObjectResponseWrapper handleConstraintViolationException(ConstraintViolationException e) {
        return ObjectResponseWrapper.builder().status(0).message(e.getMessage()).build();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status, @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        final String[] e = {""};
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
             e[0] =  errorMessage;
        });
        return ResponseEntity.badRequest().body(ObjectResponseWrapper.builder()
                .status(0)
                .message(e[0])
                .build());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
                                                             AccessDeniedException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
        jsonObject.addProperty("status", response.getStatus());
        jsonObject.addProperty("message", "Access denied");
        jsonObject.addProperty("path", request.getServletPath());
        Gson gson = new Gson();
        response.getWriter().write(jsonObject.toString());

        response.getWriter().close();
    }
}
