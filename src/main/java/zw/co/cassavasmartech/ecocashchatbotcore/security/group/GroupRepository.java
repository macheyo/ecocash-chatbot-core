package zw.co.cassavasmartech.ecocashchatbotcore.security.group;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByName(String name);

    @Query("select u from UserGroup u where u.status in('ACTIVE','INACTIVE')")
    Optional<List<UserGroup>> findAllByStatusIn();

}
