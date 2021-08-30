package zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.controller.ProfileController;
import zw.co.cassavasmartech.ecocashchatbotcore.controller.TicketController;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class TicketModelAssembler implements RepresentationModelAssembler<Ticket, EntityModel<Ticket>> {
    @Override
    public EntityModel<Ticket> toModel(Ticket entity) {
        return EntityModel.of(entity, linkTo(methodOn(TicketController.class).allTickets()).withRel("tickets"));
    }

    @Override
    public CollectionModel<EntityModel<Ticket>> toCollectionModel(Iterable<? extends Ticket> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
