package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;

public interface ProfileRepository extends JpaRepository <Profile, Long> {
    Profile getByChatId(String id);
}
