package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelfServiceCoreProcessorImpl implements SelfServiceCoreProcessor{
    private final SelfServiceCoreInvoker selfServiceCoreInvoker;

    @Override
    public Boolean isEnrolled(String msisdn) {
        return selfServiceCoreInvoker.invokeIsEnrolled(msisdn);
    }

    @Override
    public List<Answer> getAnswerByMsisdnAndAnswerStatus(String msisdn) {
        return selfServiceCoreInvoker.InvokeGetAnswerByMsisdnAndAnswerStatus(msisdn);
    }

    @Override
    public SubscriberDto getAlternative(String msisdn) {
        return selfServiceCoreInvoker.InvokeGetAlternative(msisdn);
    }
}
