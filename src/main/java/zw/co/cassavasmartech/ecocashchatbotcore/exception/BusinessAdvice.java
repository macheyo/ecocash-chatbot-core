package zw.co.cassavasmartech.ecocashchatbotcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;

@ControllerAdvice
public class BusinessAdvice {
//    @ResponseBody
//    @ExceptionHandler(BusinessException.class)
//    @ResponseStatus(HttpStatus.OK)
//    ApiResponse<?> businessExceptionHandler(BusinessException ex) {
//        ApiResponse<?> response = new ApiResponse();
//        response.setMessage(ex.getMessage());
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return response;
//    }
}
