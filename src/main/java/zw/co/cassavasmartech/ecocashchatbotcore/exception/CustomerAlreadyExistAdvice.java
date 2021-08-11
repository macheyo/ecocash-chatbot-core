package zw.co.cassavasmartech.ecocashchatbotcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;

@ControllerAdvice
public class CustomerAlreadyExistAdvice {
    @ResponseBody
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> customerAlreadyExistsHandler(CustomerAlreadyExistsException ex) {
        ApiResponse<?> response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return response;
    }
}
