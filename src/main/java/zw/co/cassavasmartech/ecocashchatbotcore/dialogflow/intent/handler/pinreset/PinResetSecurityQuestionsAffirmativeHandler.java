package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.pinreset;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.TicketParameter;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;

import java.util.List;
import java.util.Map;

@Slf4j
public class PinResetSecurityQuestionsAffirmativeHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Map<String,Object> ticket = DialogFlowUtil.getTicket(webhookRequest[0]);
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String prompt = null;
        OutputContext outputContext = null;
        if(customer!=null) {
            List<Answer> answers = DialogFlowUtil.getAnswerByMsisdnAndAnswerStatus(webhookRequest[0]);
            if(ticket.containsKey("question")){
                Double i = (Double) ticket.get("question");
                if(i.intValue()<answers.size()-1) {
                    Answer answer = answers.get(i.intValue());
                    prompt = answer.getQuestion().getText();
                    TicketParameter ticketParameter = TicketParameter.builder()
                            .id(Double.valueOf(ticket.get("id").toString()).longValue())
                            .question(i.intValue()+1)
                            .build();
                    outputContext = OutputContext.builder()
                            .lifespanCount(50)
                            .name(webhookRequest[0].getSession()+ "/contexts/ticket")
                            .parameters(ticketParameter)
                            .build();
                }else{
                    prompt="well done";
                }
            }else{
                prompt = answers.get(0).getQuestion().getText();
                TicketParameter ticketParameter = TicketParameter.builder()
                        .id(Double.valueOf(ticket.get("id").toString()).longValue())
                        .question(1)
                        .build();
                outputContext = OutputContext.builder()
                        .lifespanCount(50)
                        .name(webhookRequest[0].getSession() + "/contexts/ticket")
                        .parameters(ticketParameter)
                        .build();
            }
        }
        assert prompt != null;
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt.toString())
                .source("ecocashchatbotcore")
                .outputContexts(new Object[]{outputContext} )
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }
}
