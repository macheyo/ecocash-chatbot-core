package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.TicketNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.TicketModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
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
    CustomerRepository customerRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<EntityModel<Ticket>> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Ticket save(String chatId, Ticket ticket) {
        return customerRepository.findByProfilesChatId(chatId).map(customer -> {
            ticket.setCustomer(customer);
            log.info("new ticket created {}", ticket);
            return ticketRepository.save(ticket);
        }).orElseThrow(()->new CustomerNotFoundException(chatId));
    }

    @Override
    public Ticket update(String chatId, Long ticketId, Ticket ticket) {
        customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        return ticketRepository.findById(ticketId).map(ticketUpdate -> {
            return ticketRepository.save(modelMapper.map(ticketUpdate,Ticket.class));
        }).orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));

    }
}
