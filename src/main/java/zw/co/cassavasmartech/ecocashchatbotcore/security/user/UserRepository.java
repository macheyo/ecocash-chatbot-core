package zw.co.cassavasmartech.ecocashchatbotcore.security.user;


import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

}
