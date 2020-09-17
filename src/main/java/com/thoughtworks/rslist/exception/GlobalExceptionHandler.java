package com.thoughtworks.rslist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidIndexException.class,
                        InvalidParamException.class})
    public ResponseEntity<CommentError> handleGlobalException(Exception e) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("ERROR IS {} : {}", e.getClass(), e.getMessage());

        CommentError commentError = new CommentError();
        commentError.setError(e.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
