package zw.co.cassavasmartech.ecocashchatbotcore.sms.scheduledsms;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ScheduledSms;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ScheduledSmsServiceImpl implements ScheduledSmsService {

    private final ScheduledSmsRepository scheduledSmsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledSms> findAll(int pageSize) {

        log.debug("Retrieving all scheduled sms");
        final PageRequest pageRequest = PageRequest.of(0, pageSize);
        return scheduledSmsRepository.findAll(pageRequest).getContent();
    }

    @Override
    public void delete(Long id) {

        scheduledSmsRepository.deleteById(id);
    }

    @Override
    public ScheduledSms save(ScheduledSms scheduledSms) {

        return scheduledSmsRepository.save(scheduledSms);
    }
}
