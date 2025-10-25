package com.felixnguyen.dreamshops.exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDiedException(AccessDeniedException ex) {
    String message = "Access Denied: You do not have permission to access this resource.";
    return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
  }
}
