package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
