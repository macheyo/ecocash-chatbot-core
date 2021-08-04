package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

@Data
public class OTP extends BaseEntity{
    private String verificationCode;
}
