package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.EipService;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.ProfileNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.TicketNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.TicketModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService{
    @Autowired
    TicketModelAssembler ticketModelAssembler;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EipService eipService;
    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;
    @Override
    public List<EntityModel<Ticket>> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Ticket save(String chatId, Ticket ticket) {
        return profileRepository.findByChatId(chatId).map(profile -> {
            ticket.setProfile(profile);
            return ticketRepository.save(ticket);
        }).orElseThrow(()->new ProfileNotFoundException(chatId));
    }

    @Override
    public Ticket update(String chatId, Long ticketId, Ticket ticket) {
        profileRepository.findByChatId(chatId).orElseThrow(()->new ProfileNotFoundException(chatId));
        return ticketRepository.findById(ticketId).map(ticketUpdate -> {
            ticketUpdate.setSentimentEnd(ticket.getSentimentEnd());
            ticketUpdate.setStage(ticket.getStage());
            ticketUpdate.setTicketStatus(ticket.getTicketStatus());
            return ticketRepository.save(ticketUpdate);
        }).orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));

    }

    @Override
    public Profile findByReference(String reference) {
        return ticketRepository.findByReference(reference).orElseThrow(()->new TicketNotFoundException(reference)).getProfile();
    }

    @Override
    public Boolean handleEipCallback(EipTransaction eipTransaction) {
        return eipService.handleCallback(eipTransaction);
    }

    @Override
    public Ticket findById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(()->new TicketNotFoundException(ticketId.toString()));
    }

    @Override
    public Ticket updateSingle(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Boolean handleCpgCallback(PostTransaction postTransaction) {
        return paymentGatewayProcessor.handleCallBack(postTransaction);
    }
}
