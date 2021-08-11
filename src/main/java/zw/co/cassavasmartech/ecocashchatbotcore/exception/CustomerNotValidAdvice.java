package zw.co.cassavasmartech.ecocashchatbotcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;

@ControllerAdvice
public class CustomerNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(CustomerNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> customerNotValidHandler(CustomerNotValidException ex) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
