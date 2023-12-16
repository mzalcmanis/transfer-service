package com.mzalcmanis.transfer.api;


import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * Util for bringing meaningful error messages to the end-user,
 * For simplicity we directly use Http status codes instead of domain-specific ones.
 * Likewise, we use string error message instead of localizable code
 *
 * @param <T> payload that is present in case of successful outcome
 */
@Getter
@NoArgsConstructor(access = AccessLevel.NONE)
public class ApiResult<T> {

    private T payload;

    private HttpStatus httpStatus;

    private String errorMessage;

    public static <T> ApiResult<T> ofSuccess(T payload) {
        ApiResult<T> result = new ApiResult<>();
        result.payload = payload;
        return result;
    }

    public static <T> ApiResult<T> ofError(HttpStatus httpStatus, String errorMessage) {
        ApiResult<T> result = new ApiResult<>();
        result.errorMessage = errorMessage;
        result.httpStatus = httpStatus;
        return result;
    }

    public static <T> ApiResult<T> ofError(ApiResult<?> another) {
        return ofError(another.httpStatus, another.errorMessage);
    }

    public boolean isError() {
        return errorMessage != null;
    }

    public T get(){
        return payload;
    }

    public ResponseEntity<T> toResponseEntity(){
        if(isError()){
           return ResponseEntity.of(ProblemDetail.forStatusAndDetail(httpStatus, errorMessage)).build();
        }
        return ResponseEntity.ok(payload);
    }

}
