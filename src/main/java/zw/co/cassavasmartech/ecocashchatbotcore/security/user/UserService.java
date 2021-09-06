package zw.co.cassavasmartech.ecocashchatbotcore.security.user;


import zw.co.cassavasmartech.ecocashchatbotcore.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    Optional<User> update(User user);

    Optional<User> findByUsername(String username);

    Optional<List<User>> findAllUsers(int page, int size);
}
