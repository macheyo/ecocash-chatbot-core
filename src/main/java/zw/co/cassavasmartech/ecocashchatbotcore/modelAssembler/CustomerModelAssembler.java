package zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.controller.CustomerController;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {
    @Override
    public EntityModel<Customer> toModel(Customer entity) {
        return EntityModel.of(entity, linkTo(methodOn(CustomerController.class).allCustomers()).withRel("customers"));
    }

    @Override
    public CollectionModel<EntityModel<Customer>> toCollectionModel(Iterable<? extends Customer> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
