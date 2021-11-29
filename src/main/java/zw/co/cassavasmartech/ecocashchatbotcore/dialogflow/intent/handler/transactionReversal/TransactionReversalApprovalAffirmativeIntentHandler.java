package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.transactionReversal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;

import java.util.Objects;
import java.util.Optional;
@Slf4j
public class TransactionReversalApprovalAffirmativeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        DialogFlowUtil.validateCustomerPIN(webhookRequest[0]);
//        HttpEntity<ApiResponse<Optional<ReversalDto>>> response = DialogFlowUtil.approveTransactionReversal(webhookRequest[0]);
//        if(Objects.requireNonNull(response.getBody()).getStatus()== HttpStatus.OK.value()) return DialogFlowUtil.getResponse(webhookRequest[0],
//                DialogFlowUtil.promptProcessor(10,webhookRequest[0],customer),
//                new Object[]{},
//                UseCase.TRANSACTION_REVERSAL);
//        else return DialogFlowUtil.getResponse(webhookRequest[0],
//                DialogFlowUtil.promptProcessor(11,webhookRequest[0],customer),
//                new Object[]{},
//                UseCase.TRANSACTION_REVERSAL);
        return null;
    }
}
