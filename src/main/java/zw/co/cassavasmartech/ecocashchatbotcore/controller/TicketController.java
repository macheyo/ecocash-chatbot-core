package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.TicketModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customer")
public class TicketController {
    @Autowired
    TicketModelAssembler ticketModelAssembler;
    @Autowired
    TicketService ticketService;


    @GetMapping("/ticket/list")
    public ApiResponse<CollectionModel<EntityModel<Ticket>>> allTickets() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                CollectionModel.of(ticketService.findAll(), linkTo(methodOn(ProfileController.class).allProfiles()).withSelfRel()));
    }

    @PostMapping("/{chatId}/ticket")
    public ApiResponse<EntityModel<Ticket>> createNewTicket(@PathVariable String chatId, @Valid @RequestBody Ticket ticket){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                ticketModelAssembler.toModel(ticketService.save(chatId, ticket)));
    }

    @PutMapping("/{chatId}/ticket/{ticketId}")
    public ApiResponse<EntityModel<Ticket>> updateTicket(@PathVariable String chatId, @PathVariable Long ticketId,@Valid @RequestBody Ticket ticket){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                ticketModelAssembler.toModel(ticketService.update(chatId,ticketId,ticket)));
    }
}
