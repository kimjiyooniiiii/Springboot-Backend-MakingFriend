package com.knucapstone.rudoori.common;


import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.knucapstone.rudoori"})
public class CommonExceptionHandler {
    @ExceptionHandler({DuplicateKeyException.class, DuplicateRequestException.class})
    public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<?>> handleNullPointerException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.createError(exception.getMessage()));
    }
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResponse<?>> handExpiredJwtException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(exception.getMessage()));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError("비밀번호 혹은 로그인 입력 오류"));
    }

    @ExceptionHandler(NonSelfException.class)
    public ResponseEntity<ApiResponse<?>> handleNonSelfException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.createError(exception.getMessage()));
    }
}
