package zw.co.cassavasmartech.ecocashchatbotcore.process;

import zw.co.cassavasmartech.esb.model.PaymentMethod;
import zw.co.cassavasmartech.esb.model.ProcessRegister;

import java.util.List;
import java.util.Optional;

public interface ProcessRegisterService {

    Optional<ProcessRegister> save(ProcessRegister processRegister);

    Optional<ProcessRegister> update(ProcessRegister processRegister);

    Optional<ProcessRegister> findByCodeAndPaymentMethodAndEnabled(String code, PaymentMethod paymentMethod, boolean enabled);

    List<ProcessRegister> findAll(int page, int size);

    Optional<ProcessRegister> findByCodeAndEnabled(String code);


}
