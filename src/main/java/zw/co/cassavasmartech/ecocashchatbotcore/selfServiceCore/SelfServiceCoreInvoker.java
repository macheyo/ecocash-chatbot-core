package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.util.List;

public interface SelfServiceCoreInvoker {
    Boolean invokeIsEnrolled(String msisdn);
    List<Answer> InvokeGetAnswerByMsisdnAndAnswerStatus(String msisdn);
    SubscriberDto InvokeGetAlternative(String msisdn);
}
