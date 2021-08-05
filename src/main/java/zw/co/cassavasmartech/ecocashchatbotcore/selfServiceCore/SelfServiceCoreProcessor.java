package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.util.List;

public interface SelfServiceCoreProcessor {
    public Boolean isEnrolled(String msisdn);
    public List<Answer> getAnswerByMsisdnAndAnswerStatus(String msisdn);
    public SubscriberDto getAlternative(String msisdn);
}
