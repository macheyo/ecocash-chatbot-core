package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;

import java.util.List;

@Service
public interface TicketService {
    List<EntityModel<Ticket>> findAll();

    Ticket save(String msisdn, Ticket ticket);

    Ticket update(String chatId, Long ticketId, Ticket ticket);
}
