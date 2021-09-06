package zw.co.cassavasmartech.ecocashchatbotcore.security.partner;


import org.springframework.data.repository.PagingAndSortingRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;

import java.util.Optional;

public interface PartnerRepository extends PagingAndSortingRepository<Partner, Long> {

    Optional<Partner> findByPartnerId(String partnerId);

}
