package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerAlreadyExistsException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerModelAssembler assembler;


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ApiResponse<EntityModel<Customer>> createNewCustomer(@Valid @RequestBody Customer customer) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                assembler.toModel(customerService.save(customer)));
    }

    @GetMapping("/{id}")
    public EntityModel<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id).orElseThrow(() -> new CustomerNotFoundException(id.toString()));
        return assembler.toModel(customer);
    }

    @GetMapping("/profile/{chatId}")
    public ApiResponse<EntityModel<Customer>> getCustomerByChatId(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                assembler.toModel(customerService.getByChatId(chatId))
        );
    }

    @GetMapping("/list")
    public ApiResponse<CollectionModel<EntityModel<Customer>>> allCustomers() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                CollectionModel.of(customerService.findAll(),linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel()));
    }

    @GetMapping("/is-enrolled/{chatId}")
    public ApiResponse<Boolean> isEnrolled(@PathVariable String chatId)
    {
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.isEnrolled(chatId));
    }

    @GetMapping("/security/question/list/{chatId}")
    public ApiResponse<CollectionModel<Answer>> getAnswer(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                CollectionModel.of(customerService.getAnswers(chatId),linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel()));
    }

    @GetMapping("/alternative/{chatId}")
    public ApiResponse<?> getAlternative(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.getAlternative(chatId));
    }

    @GetMapping("/statement/{chatId}")
    public ApiResponse<Statement> getStatement(@PathVariable String chatId, @Valid @RequestBody StatementRequest statementRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.getStatement(chatId, statementRequest));
    }

    @GetMapping("/pinreset/{chatId}")
    public ApiResponse<Boolean> pinreset(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.pinReset(chatId));

    }

}
