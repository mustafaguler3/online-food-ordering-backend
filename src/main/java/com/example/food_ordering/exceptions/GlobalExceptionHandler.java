package com.example.food_ordering.exceptions;

import com.example.food_ordering.dto.ErrorDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimestamp(new Timestamp(System.currentTimeMillis() *10 *60).toLocalDateTime());
        errorDto.setPath(request.getDescription(false));
        errorDto.setMessage(ex.getMessage());
        errorDto.setTrace(ex.getStackTrace().toString());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleResourceNotFoundException(NullPointerException exception) {


        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            log.error("Exception occurred in class: {}, method: {}, line: {}",
                    element.getClassName(), element.getMethodName(), element.getLineNumber());
        }
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
}




















