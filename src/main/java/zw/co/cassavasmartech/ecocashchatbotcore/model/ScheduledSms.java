package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "scheduled_sms")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Access(AccessType.FIELD)
public class ScheduledSms extends BaseEntity {

    @Column(name = "msisdn", length = 12, nullable = false)
    private String msisdn;

    @Column(name = "text", length = 500, nullable = false)
    private String text;

    public ScheduledSms(String msisdn, String text) {
        this.msisdn = msisdn;
        this.text = text;
    }


    public ScheduledSms() {
    }
}
