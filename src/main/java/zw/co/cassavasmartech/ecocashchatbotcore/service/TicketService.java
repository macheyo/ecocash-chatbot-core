package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;

import java.util.List;

@Service
public interface TicketService {
    List<EntityModel<Ticket>> findAll();

    Ticket save(String msisdn, Ticket ticket);

    Ticket update(String chatId, Long ticketId, Ticket ticket);

    Ticket findByReference(String reference);

    Ticket findByReferenceAndTicketStatus(String reference, TicketStatus ticketStatus);

    Profile findProfileByReference(String reference);

    Boolean handleEipCallback(EipTransaction eipTransaction);

    Ticket findById(Long ticketId);

    Ticket updateSingle(Ticket ticket);

    Boolean handleCpgCallback(PostTransaction postTransaction);

    Boolean handlePINCallback(PostTransaction postTransaction);
}
