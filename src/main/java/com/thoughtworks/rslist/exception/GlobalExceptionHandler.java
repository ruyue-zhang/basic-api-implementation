package com.thoughtworks.rslist.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidIndexException.class})
    public ResponseEntity<CommentError> handleGlobalException(Exception e) {
        CommentError commentError = new CommentError();
        commentError.setError(e.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
