package zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.controller.CustomerController;
import zw.co.cassavasmartech.ecocashchatbotcore.controller.ProfileController;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {
    @Override
    public EntityModel<Profile> toModel(Profile entity) {
        return EntityModel.of(entity, linkTo(methodOn(ProfileController.class).allProfiles()).withRel("profiles"));
    }

    @Override
    public CollectionModel<EntityModel<Profile>> toCollectionModel(Iterable<? extends Profile> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
