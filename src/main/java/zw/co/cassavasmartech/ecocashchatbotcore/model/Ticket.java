package zw.co.cassavasmartech.ecocashchatbotcore.model;

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
    private Usecase usecase;
    private int stage;
    private Double sentimentStart;
    private Double sentimentEnd;
    private Status status;
    private String refrence;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;
}
