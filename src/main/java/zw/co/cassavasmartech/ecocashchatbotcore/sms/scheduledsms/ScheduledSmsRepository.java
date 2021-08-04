package zw.co.cassavasmartech.ecocashchatbotcore.sms.scheduledsms;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ScheduledSms;

import java.util.List;

public interface ScheduledSmsRepository extends JpaRepository<ScheduledSms, Long> {

    List<ScheduledSms> findByMsisdn(String msisdn);

    Page<ScheduledSms> findAll(Pageable pageable);

}
