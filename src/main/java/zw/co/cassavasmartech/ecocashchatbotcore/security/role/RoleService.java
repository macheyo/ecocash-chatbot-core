package zw.co.cassavasmartech.ecocashchatbotcore.security.role;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<Role> save(Role role);

    Optional<Role> update(Role role);

    Optional<Role> findByName(String name);

    Optional<Role> findById(Long id);

    Optional<List<Role>> findAll();
}
