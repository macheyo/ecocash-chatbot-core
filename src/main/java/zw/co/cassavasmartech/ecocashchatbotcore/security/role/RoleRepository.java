package zw.co.cassavasmartech.ecocashchatbotcore.security.role;


import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
