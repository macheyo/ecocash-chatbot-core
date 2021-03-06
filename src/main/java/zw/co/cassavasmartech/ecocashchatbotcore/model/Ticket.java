package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Ticket extends BaseEntity{
    private String originalQueryText;
    private UseCase usecase;
    private int stage;
    private Double sentimentStart;
    private Double sentimentEnd;
    private TicketStatus ticketStatus;
    private String reference;
    private String conversationId;
    private String folio;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;
}
