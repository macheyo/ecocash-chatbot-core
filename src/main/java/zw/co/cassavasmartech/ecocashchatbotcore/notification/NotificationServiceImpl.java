package zw.co.cassavasmartech.ecocashchatbotcore.notification;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsProperties;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SmsProperties smsProperties;
    private final MobileNumberFormater mobileNumberFormater;
    private final CoreInvoker coreInvoker;


    @Override
    public void sendSms(Sms sms) {
            sms.setFrom(smsProperties.getSender());
            sms.setTo(mobileNumberFormater.formatMobileNumberInternational(sms.getTo()));
            coreInvoker.invoke(sms,
                    "https://notifications.cassavafintech.com/notification/send-sms",
                    HttpMethod.POST,
                    new ParameterizedTypeReference<ApiResponse<Sms>>() {});
    }


}
