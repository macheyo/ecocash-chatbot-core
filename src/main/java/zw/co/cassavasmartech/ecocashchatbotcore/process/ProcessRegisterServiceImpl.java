package zw.co.cassavasmartech.ecocashchatbotcore.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentMethod;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ProcessRegister;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProcessRegisterServiceImpl implements ProcessRegisterService {

    private final ProcessRegisterRepository processRegisterRepository;

    public ProcessRegisterServiceImpl(ProcessRegisterRepository processRegisterRepository) {
        this.processRegisterRepository = processRegisterRepository;
    }

    @Override
    public Optional<ProcessRegister> save(ProcessRegister processRegister) {
        return Optional.ofNullable(processRegisterRepository.save(processRegister));
    }

    @Override
    public Optional<ProcessRegister> update(ProcessRegister processRegister) {
        return this.save(processRegister);
    }

    @Override
    public Optional<ProcessRegister> findByCodeAndPaymentMethodAndEnabled(String code, PaymentMethod paymentMethod, boolean enabled) {
        return processRegisterRepository.findByCodeAndPaymentMethodAndEnabled(code, paymentMethod, enabled);
    }

    @Override
    public List<ProcessRegister> findAll(int page, int size) {
        return processRegisterRepository.findAll(PageRequest.of(page, size, Sort.by("code"))).getContent();
    }

    @Override
    public Optional<ProcessRegister> findByCodeAndEnabled(String code) {
        return processRegisterRepository.findByCodeAndEnabled(code,true);
    }

}
