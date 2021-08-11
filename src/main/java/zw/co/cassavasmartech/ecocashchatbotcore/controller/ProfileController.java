package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.OTP;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.ProfileModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/customer")
public class ProfileController {
    @Autowired
    ProfileModelAssembler profileModelAssembler;

    @Autowired
    CustomerModelAssembler customerModelAssembler;

    @Autowired
    ProfileService profileService;

    @PostMapping("/{msisdn}/profile")
    public ApiResponse<EntityModel<Profile>> createNewProfile(@PathVariable String msisdn, @Valid @RequestBody Profile profile){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                profileModelAssembler.toModel(profileService.save(msisdn, profile)));
    }

    @GetMapping("/profile/list")
    public ApiResponse<CollectionModel<EntityModel<Profile>>> allProfiles() {
        List<EntityModel<Profile>> profiles = profileService.findAll();
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                CollectionModel.of(profiles, linkTo(methodOn(ProfileController.class).allProfiles()).withSelfRel()));
    }

    @GetMapping("/profile/otp/generate/{chatId}")
    public ApiResponse<Boolean> generateOTP(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                profileService.generateOtp(chatId));
    }

    @PostMapping("/profile/otp/verify/{chatId}")
    public ApiResponse<Boolean> verifyCustomer(@PathVariable String chatId, @RequestBody OTP otp){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                profileService.verifyCustomer(chatId, otp.getVerificationCode()));

    }



}

