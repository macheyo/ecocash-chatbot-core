package zw.co.cassavasmartech.ecocashchatbotcore.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String SYSTEM_USER = "system";

    @Override
    public Optional<String> getCurrentAuditor() {

        final Optional<String> optionalUser = SecurityUtils.getCurrentUserLogin();
        return optionalUser.map(user -> {
            if (user.equals(ANONYMOUS_USER)) {
                return Optional.of(SYSTEM_USER);
            }
            return optionalUser;
        }).orElse(Optional.ofNullable(SYSTEM_USER));
    }
}
