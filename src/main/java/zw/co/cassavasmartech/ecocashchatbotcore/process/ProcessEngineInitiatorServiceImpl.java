package zw.co.cassavasmartech.ecocashchatbotcore.process;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessEngineInitiatorServiceImpl implements ProcessEngineInitiatorService {

    @Autowired
    private ProcessEngine camunda;

    @Override
    @Async
    public void process(final String businessKey){
        try {
            log.info("\n\nSending message to service task to process callback message :{}",businessKey);
            camunda.getRuntimeService().createMessageCorrelation(ProcessConstants.MSG_KEY_EIP_CALLBACK_RECEIVED)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } catch (Exception e) {
            log.error("\n\nFailed to correlate message because :{}", e);
        }

    }
}
