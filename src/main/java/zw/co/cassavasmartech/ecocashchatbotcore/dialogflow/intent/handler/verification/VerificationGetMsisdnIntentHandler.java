package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.verification;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

import java.util.Optional;
@Slf4j
public class VerificationGetMsisdnIntentHandler extends IntentHandlerAdapter {

    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = new Customer();
        String prompt;
        Object[] context = null;
        customer.setMsisdn(DialogFlowUtil.getMinimumMsisdn(webhookRequest[0].getQueryResult().getQueryText()));
        if (!DialogFlowUtil.findCustomerByMsisdn(customer.getMsisdn()).isPresent()) DialogFlowUtil.saveCustomer(customer);
        if (DialogFlowUtil.customerLookup(SubscriberDto.builder().msisdn(customer.getMsisdn()).build()).getField1().equalsIgnoreCase("200")) {
            if (!DialogFlowUtil.findCustomerProfileByChatId(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest())).isPresent()) {
                Optional<Customer> c = DialogFlowUtil.findCustomerByMsisdn(customer.getMsisdn());
                Profile profile = new Profile();
                profile.setAlias(DialogFlowUtil.getPlatformName(webhookRequest[0].getOriginalDetectIntentRequest(), c));
                profile.setChatId(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest()));
                profile.setPlatform(DialogFlowUtil.getPlatform(webhookRequest[0].getOriginalDetectIntentRequest()));
                DialogFlowUtil.saveProfile(c.get().getMsisdn(), profile);
                prompt = DialogFlowUtil.promptProcessor(1, webhookRequest[0], customer);
                DialogFlowUtil.generateOTP(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest()));
            } else {
                prompt = DialogFlowUtil.promptProcessor(1, webhookRequest[0], customer);
                DialogFlowUtil.generateOTP(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest()));
            }
        } else
            prompt = DialogFlowUtil.promptProcessor(2, webhookRequest[0], customer);
        return DialogFlowUtil.getResponse(webhookRequest[0], prompt, context, Usecase.VERIFICATION);
    }
}
