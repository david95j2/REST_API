package com.example.restapi.configuration;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> app(AppException e) {
        log.error("Error occurs : {}",e.toString());
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode",e.getErrorCode());
        errorResponse.put("status",e.getErrorCode().getStatus().value());
        errorResponse.put("message",e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    /**
     * @valid  유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodValidException(MethodArgumentNotValidException e){
        log.error("Error occurs : {}",e.toString());
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", ErrorCode.INCORRECT.getStatus());
        errorResponse.put("status",ErrorCode.INCORRECT.getStatus().value());
        errorResponse.put("message",e.getBindingResult().getFieldError().getDefaultMessage());

        return ResponseEntity
                .status(ErrorCode.INCORRECT.getStatus())
                .body(errorResponse);
    }
}
