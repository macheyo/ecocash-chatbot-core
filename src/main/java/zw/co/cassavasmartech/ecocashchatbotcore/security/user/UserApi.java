package zw.co.cassavasmartech.ecocashchatbotcore.security.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.User;
import zw.co.cassavasmartech.ecocashchatbotcore.security.user.data.UserDTO;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserApi {

    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;


    @PostMapping
    public ApiResponse<UserDTO> save(@RequestBody UserDTO userDTO) {
        userDTO.setEnabled(true);
        Optional<User> optionalUser = userService.save(userConverter.dtoToUser(userDTO));
        User user = optionalUser.orElseThrow(() -> new BusinessException("Internal Error Occurred While Creating User"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userConverter.userToDto(user));
    }

    @PostMapping("/update")
    public ApiResponse<UserDTO> update(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userService.update(userConverter.dtoToUser(userDTO));
        User user = optionalUser.orElseThrow(() -> new BusinessException("Internal Error Occurred While Updating User"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userConverter.userToDto(user));
    }

    @GetMapping("/{username}")
    public ApiResponse<UserDTO> findByUsername(@PathVariable("username") String username) {

        Optional<User> optionalUser = userService.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new BusinessException("User:" + username + " Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userConverter.userToDto(user));
    }

    @GetMapping("/list/{page}/{size}")
    public ApiResponse<List<UserDTO>> findAll(@PathVariable("page") int page, @PathVariable("size") int size) {
        List<User> users = userService.findAllUsers(page, size).orElse(Collections.emptyList());
        List<UserDTO> userDTOList = users.stream().map(u -> userConverter.userToDto(u)).collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userDTOList);
    }
}
