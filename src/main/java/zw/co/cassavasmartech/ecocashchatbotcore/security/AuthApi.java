package zw.co.cassavasmartech.ecocashchatbotcore.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.security.payload.LoginRequest;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants.SUCCESS_MESSAGE;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthApi {


    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/generatetoken")
    public ApiResponse<String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Generate Token Request....");
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ApiConstants.USERNAME_OR_PASSWORD_INVALID, null);
        }
        final String jwt = tokenProvider.generateToken(loginRequest);

        return new ApiResponse<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, jwt);
    }
}