package zw.co.cassavasmartech.ecocashchatbotcore.security.user.data;

import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup;


@Data
public class UserDTO {

    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean firstLogIn;
    private String email;
    private String msisdn;
    private UserGroup group;

}
