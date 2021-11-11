package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.pinreset;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.TicketParameter;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TicketStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
@Slf4j
public class PinResetSecurityQuestionsAnswer extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Map<String,Object> ticket = DialogFlowUtil.getTicket(webhookRequest[0]);
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String prompt = null;
        OutputContext outputContext = null;
        String customerAnswer = webhookRequest[0].getQueryResult().getQueryText().toLowerCase(Locale.ROOT);
        //customerAnswer = DialogFlowUtil.encodeText(customerAnswer);
        OutputContext outputContext1=null;
        if(customer!=null) {
            List<Answer> answers = DialogFlowUtil.getAnswerByMsisdnAndAnswerStatus(webhookRequest[0]);
            Double i = (Double) ticket.get("question");
            Answer answer = answers.get(i.intValue()-1);
            if(DialogFlowUtil.isEncodingMatch(answer,customerAnswer)){
                if(i.intValue()<answers.size()) {
                    Answer newAnswer = answers.get(i.intValue());
                    prompt = newAnswer.getQuestion().getText();
                    TicketParameter ticketParameter = TicketParameter.builder()
                            .id(Double.valueOf(ticket.get("id").toString()).longValue())
                            .question(i.intValue()+1)
                            .build();
                    outputContext = OutputContext.builder()
                            .lifespanCount(50)
                            .name(webhookRequest[0].getSession() + "/contexts/ticket")
                            .parameters(ticketParameter)
                            .build();
                    outputContext1 = OutputContext.builder()
                            .lifespanCount(1)
                            .name(webhookRequest[0].getSession() + "/contexts/awaiting_answers")
                            .build();
                }else{
                    if(DialogFlowUtil.resetPIN(webhookRequest[0])) {
                        prompt = DialogFlowUtil.promptProcessor(5, webhookRequest[0], customer);// "well done" + Emoji.ThumbsUp + "I have reset your PIN. You will receive an SMS on your phone with your new temporary PIN. You will be prompted to change this when you dial *151#. \nAnything else I can do for you" + Emoji.Smiley;
                        outputContext = OutputContext.builder()
                                .lifespanCount(50)
                                .name(webhookRequest[0].getSession() + "/contexts/ticket")
                                .build();
                        outputContext1 = OutputContext.builder()
                                .lifespanCount(1)
                                .name(webhookRequest[0].getSession() + "/contexts/awaiting_pinreset_more")
                                .build();
                        return WebhookResponse.builder()
                                .fulfillmentText(prompt)
                                .outputContexts(new Object[]{outputContext,outputContext1})
                                .source("ecocashchatbotcore")
                                .build();
                    }else {
                        prompt = "Oops" + Emoji.Pensive + "An error occured processing your request please try again later";
                        return WebhookResponse.builder()
                                .fulfillmentText(prompt)
                                .source("ecocashchatbotcore")
                                .build();
                    }
                }
            } else {
                prompt = DialogFlowUtil.promptProcessor(6,webhookRequest[0],customer);
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(DialogFlowUtil.closeTicket(webhookRequest[0], TicketStatus.ESCALATED))
                        .build();
            }
        }
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .outputContexts(new Object[]{outputContext,outputContext1})
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }
}
