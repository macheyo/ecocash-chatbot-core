package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class BaseEntity extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false, updatable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

}