package zw.co.cassavasmartech.ecocashchatbotcore.selfservice;

import org.springframework.http.HttpEntity;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.EnrollmentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.EcocashTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;

import java.util.List;
import java.util.Optional;

public interface SelfServiceCoreProcessor {
    public EnrollmentResponse isEnrolled(String msisdn);
    public List<Answer> getAnswerByMsisdnAndAnswerStatus(String msisdn);
    public SubscriberDto getAlternative(String msisdn);
    public HttpEntity<ApiResponse<Optional<EcocashTransaction>>> validateReversal(String msisdn, String reference);
    public HttpEntity<ApiResponse<Optional<ReversalDto>>> initiateReversal(String msisdn, String reference);
    public HttpEntity<ApiResponse<List<ReversalDto>>> pendingReversals(String msisdn);
}
