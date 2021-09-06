package zw.co.cassavasmartech.ecocashchatbotcore.security.role;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Role;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> save(Role role) {
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> update(Role role) {
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<List<Role>> findAll() {
        return Optional.ofNullable(roleRepository.findAll(Sort.by(Sort.Order.asc("description"))));
    }
}
