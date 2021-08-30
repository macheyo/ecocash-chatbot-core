package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;

import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface ProfileRepository extends JpaRepository <Profile, Long> {
    Optional<Profile> getByChatId(String chatId);

    Optional<Profile> findByChatId(String chatId);
}
