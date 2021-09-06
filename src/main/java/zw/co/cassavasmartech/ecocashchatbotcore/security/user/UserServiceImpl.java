package zw.co.cassavasmartech.ecocashchatbotcore.security.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${ecocash.chatbot.core.ldap.searchFilter.userExists.accountName}")
    private String searchFilterAccountName;
    @Value("${ecocash.chatbot.core.ldap.searchFilter.userExists.objectclass}")
    private String searchFilterObjectClass;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        if (!userNameExistsInActiveDirectory(user.getUsername())) {
            throw new NameNotFoundException("User cannot be saved, name does not exist in Active Directory");
        }
        user.setEnabled(true);
        user = userRepository.save(user);
        return Optional.ofNullable(userRepository.save(user));
    }

    @Override
    public Optional<User> update(User user) {

        log.info("Update User {}", user);
        Optional<User> optionalUser = this.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            throw new BusinessException("Error Updating Record. User Not Found");
        }
        User userUpdate = optionalUser.get();

        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getMsisdn() != null) {
            userUpdate.setMsisdn(user.getMsisdn());
        }
        if (user.getUserGroup() != null) {
            userUpdate.setUserGroup(user.getUserGroup());
        }
        userUpdate.setEnabled(user.isEnabled());
        return Optional.ofNullable(userRepository.save(userUpdate));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }

    @Override
    public Optional<List<User>> findAllUsers(int page, int size) {
        return Optional.ofNullable(userRepository.findAll(PageRequest.of(page, size, Sort.by("username"))).getContent());
    }

    private boolean userNameExistsInActiveDirectory(String username) {

        final String searchQuery = String.format(searchFilterAccountName, username);
        ldapTemplate.setIgnorePartialResultException(true);

        return !ldapTemplate.search("", String.format(searchFilterObjectClass, searchQuery),
                (AttributesMapper<Object>) attributes -> new Object()).isEmpty();
    }

}
