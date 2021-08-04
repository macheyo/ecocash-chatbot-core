package zw.co.cassavasmartech.ecocashchatbotcore.sms.scheduledsms;


import zw.co.cassavasmartech.ecocashchatbotcore.model.ScheduledSms;

import java.util.List;

public interface ScheduledSmsService {

    List<ScheduledSms> findAll(int pageSize);

    void delete(Long id);

    ScheduledSms save(ScheduledSms scheduledSms);

}
