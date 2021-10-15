package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.pinreset;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

import java.util.Map;

@Slf4j
public class PinResetSecurityQuestionsAffirmativeHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Map<String,Object> ticket = DialogFlowUtil.getTicket(webhookRequest[0]);
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String prompt;
        Object[] context = null;
        if(customer!=null) {
            if (DialogFlowUtil.isEnrolled(customer)) {
                prompt = DialogFlowUtil.promptProcessor(1, webhookRequest[0], customer);
                context = DialogFlowUtil.createTicket(webhookRequest[0], UseCase.PIN_RESET);
            }
            else prompt = DialogFlowUtil.promptProcessor(2,webhookRequest[0],customer);
        }
        else prompt = DialogFlowUtil.promptProcessor(3, webhookRequest[0], customer);
        return DialogFlowUtil.getResponse(webhookRequest[0],prompt,context, UseCase.PIN_RESET);








//        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
//        OutputContext outputContext=null;
//        String prompt;
//        if(customer.isPresent()) {
//            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
//            if(map.containsKey("question")){
//                Double i = (Double) map.get("question");
//                if(i.intValue()<answers.size()-1) {
//                    Answer answer = answers.get(i.intValue());
//                    prompt = answer.getQuestion().getText();
//                    TicketParameter ticketParameter = TicketParameter.builder()
//                            .id(Double.valueOf(map.get("id").toString()).longValue())
//                            .question(i.intValue()+1)
//                            .build();
//                    outputContext = OutputContext.builder()
//                            .lifespanCount(50)
//                            .name(webhookRequest.getSession() + "/contexts/ticket")
//                            .parameters(ticketParameter)
//                            .build();
//                }else{
//                    prompt="well done";
//                }
//            }else{
//                prompt = answers.get(0).getQuestion().getText();
//                TicketParameter ticketParameter = TicketParameter.builder()
//                        .id(Double.valueOf(map.get("id").toString()).longValue())
//                        .question(1)
//                        .build();
//                outputContext = OutputContext.builder()
//                        .lifespanCount(50)
//                        .name(webhookRequest.getSession() + "/contexts/ticket")
//                        .parameters(ticketParameter)
//                        .build();
//            }
//        }
//        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+ Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
//        WebhookResponse webhookResponse = WebhookResponse.builder()
//                .fulfillmentText(prompt.toString())
//                .source("ecocashchatbotcore")
//                .outputContexts(new Object[]{outputContext} )
//                .build();
//        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
//        return webhookResponse;
    }
}
