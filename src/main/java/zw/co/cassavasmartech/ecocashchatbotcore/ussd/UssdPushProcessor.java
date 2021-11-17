package zw.co.cassavasmartech.ecocashchatbotcore.ussd;

import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;

public interface UssdPushProcessor {
    Boolean handleCallBack();
}
