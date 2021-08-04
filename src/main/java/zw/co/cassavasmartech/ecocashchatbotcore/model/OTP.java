package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class OTP extends BaseEntity{
    private String verificationCode;
}
