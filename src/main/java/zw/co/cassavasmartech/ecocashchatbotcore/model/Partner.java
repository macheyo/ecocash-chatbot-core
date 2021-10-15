package zw.co.cassavasmartech.ecocashchatbotcore.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Status;

import javax.persistence.*;

@Entity
@Table(name = "partner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Partner extends BaseEntity {

    @Column(name = "partner_id", nullable = false)
    private String partnerId;
    @Column(name = "partner_key",nullable = false)
    private String partnerKey;
    @Column(name = "partner_name",nullable = false)
    private String partnerName;
    @Column(name = "email",nullable = false)
    private String email;
    @Column( name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "sms_header_text")
    private String smsHeaderText;
}
