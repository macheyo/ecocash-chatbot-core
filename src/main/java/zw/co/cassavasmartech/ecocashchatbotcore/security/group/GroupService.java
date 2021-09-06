package zw.co.cassavasmartech.ecocashchatbotcore.security.group;


import zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Optional<UserGroup> save(UserGroup userGroup);

    Optional<UserGroup> update(UserGroup userGroup);

    Optional<List<UserGroup>> findAll();

    Optional<UserGroup> findByName(String name);

    Optional<UserGroup> findById(Long id);

}
