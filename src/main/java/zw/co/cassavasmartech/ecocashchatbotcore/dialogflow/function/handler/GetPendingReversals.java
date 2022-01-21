package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;

import java.util.List;

public class GetPendingReversals extends FunctionAdapter {
    public boolean hasArgs() { return true; }
    @Override
    public String process(PromptObject... args) {
        List<ReversalDto> pendingReversals = DialogFlowUtil.getPendingReversals(args[0].getWebhookRequest());
        String prompt="";
        int count=1;
        for (ReversalDto pendingReversal:pendingReversals){
            prompt += count +". "+pendingReversal.getReference()+" "+pendingReversal.getOriginalSenderMobileNumber()+"("+ DialogFlowUtil.getCustomerNameAndSurnameByMsisdn(pendingReversal.getOriginalSenderMobileNumber())+")"+" of $RTGS"+pendingReversal.getAmount()+"\n";
            count++;
        }
        return prompt;
    }
}
