package zw.co.cassavasmartech.ecocashchatbotcore.notification;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ScheduledSms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsDispatchStrategy;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.scheduledsms.ScheduledSmsService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final SmsProperties smsProperties;
    private final ScheduledSmsService scheduledSmsService;
    private final MobileNumberFormater mobileNumberFormater;


    @Override
    public void sendSms(Sms sms, SmsDispatchStrategy dispatchStrategy) {

        if (dispatchStrategy.equals(SmsDispatchStrategy.DISPATCH_NOW) || isDispatchEnabledForTime(LocalDateTime.now())) {
            sms.setFrom(smsProperties.getSender());
            sms.setTo(mobileNumberFormater.formatMobileNumberInternational(sms.getTo()));
            rabbitTemplate.convertAndSend(smsProperties.getSmsQueueName(), sms);
        } else {
            scheduleSms(sms);
        }

    }

    @Override
    public void sendSms(String to, SmsDispatchStrategy dispatchStrategy, String message) {

        final Sms sms = new Sms(mobileNumberFormater.formatMobileNumberInternational(to), message);
        this.sendSms(sms, dispatchStrategy);
    }

    private void scheduleSms(Sms sms) {

        final ScheduledSms scheduledSms = new ScheduledSms(sms.getTo(), sms.getText());
        log.debug("scheduling sms for subscriber {}", scheduledSms.getMsisdn());
        scheduledSmsService.save(scheduledSms);
    }

    private boolean isDispatchEnabledForTime(LocalDateTime localDateTime) {

        final CronSequenceGenerator cronTrigger = new CronSequenceGenerator(smsProperties.getSmsDispatchPeriodCron());
        final LocalDateTime nextExecutionTime = cronTrigger.next(new Date()).toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return nextExecutionTime.isEqual(localDateTime.plusMinutes(1).withSecond(0).withNano(0));

    }

}
