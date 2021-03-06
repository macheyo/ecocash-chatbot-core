package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByMerchantCode(String merchantCode);
    Optional<Merchant> findByName(String name);
    Optional<Merchant> findByNameOrMerchantCode(String searchQuery,String searchQueryToo);
}
