package com.atanriverdi.foreignexchange.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExchangeException.class)
    public ResponseEntity<ErrorMessage> handleExceptions(ExchangeException ex) {
        log.error("Business error happened. ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleExceptions(Throwable e) {
        log.error("Unexpected error occurred!", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Unexpected error occurred while reading the http message!", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpServletRequest req = (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
        log.error("Source not found [" + req.getRequestURI() + "]", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(String.valueOf(HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        HttpServletRequest req = (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
        log.error("Access denied to the following URI: [" + req.getRequestURI() + "]", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(String.valueOf(HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN.getReasonPhrase()));
    }

    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Unsupported mime type: [" + ex.getMessage() + "]", ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorMessage(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Unsupported http method: [" + ex.getMessage() + "]", ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorMessage(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            sb.append("[");
            sb.append(((FieldError) error).getField());
            sb.append(" ");
            sb.append(error.getDefaultMessage());
            sb.append("] ");
        });

        log.error("Request payload is not valid! " + sb.toString(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                sb.toString()));
    }
}
