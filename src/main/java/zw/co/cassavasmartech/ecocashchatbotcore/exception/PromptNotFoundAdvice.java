package zw.co.cassavasmartech.ecocashchatbotcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;

@ControllerAdvice
public class PromptNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(PromptNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    WebhookResponse promptNotFoundHandler(PromptNotFoundException ex) {
        WebhookResponse response = WebhookResponse.builder()
                .fulfillmentText("Ooops"+ Emoji.Pensive+"this service seems not to be available at the moment. Please try again later")
                .source("ecocashchatbotcore")
                .build();
        return response;
    }
}
