package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import jdk.nashorn.internal.objects.annotations.Optimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByReference(String reference);
}
