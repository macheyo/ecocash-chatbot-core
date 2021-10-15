package zw.co.cassavasmartech.ecocashchatbotcore.process;

import org.springframework.scheduling.annotation.Async;

public interface ProcessEngineInitiatorService {
    @Async
    void process(String businessKey);
}
