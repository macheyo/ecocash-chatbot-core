package zw.co.cassavasmartech.ecocashchatbotcore.security.user;


import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.User;
import zw.co.cassavasmartech.ecocashchatbotcore.security.user.data.UserDTO;

@Component
public class UserConverter {

    public User dtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEnabled(userDTO.isEnabled());
        user.setEmail(userDTO.getEmail());
        user.setMsisdn(userDTO.getMsisdn());
        user.setUserGroup(userDTO.getGroup());
        return user;
    }

    public UserDTO userToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setMsisdn(user.getMsisdn());
        userDTO.setGroup(user.getUserGroup());
        userDTO.setFirstLogIn(user.isFirstLogIn());
        return userDTO;
    }

}
