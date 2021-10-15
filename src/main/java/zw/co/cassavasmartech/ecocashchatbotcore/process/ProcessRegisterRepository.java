package zw.co.cassavasmartech.ecocashchatbotcore.process;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentMethod;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ProcessRegister;


import java.util.Optional;

public interface ProcessRegisterRepository extends JpaRepository<ProcessRegister, Long> {

    Optional<ProcessRegister> findServiceTypeByCode(String code);

    Optional<ProcessRegister> findServiceTypeByCodeAndEnabled(String code, boolean enabled);

    Optional<ProcessRegister> findByCodeAndPaymentMethodAndEnabled(String code, PaymentMethod paymentMethod, boolean enabled);

    Optional<ProcessRegister> findByCodeAndEnabled(String code, boolean enabled);
}
