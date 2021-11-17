package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.TicketModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
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

    @PostMapping("/ticket/eip/callback")
    public ApiResponse<Boolean> eipCallback(@Valid @RequestBody EipTransaction eipTransaction){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                ticketService.handleEipCallback(eipTransaction));
    }

    @PostMapping("/ticket/cpg/callback")
    public ApiResponse<Boolean> cpgCallback(@Valid @RequestBody PostTransaction postTransaction){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                ticketService.handleCpgCallback(postTransaction));
    }

    @PostMapping("/ticket/pin/callback")
    public ApiResponse<Boolean> pinCallback(){

        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                true);
    }

}
