package zw.co.cassavasmartech.ecocashchatbotcore.token.data;

import lombok.Data;

@Data
public class TokenRequest {

    private String username;
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
