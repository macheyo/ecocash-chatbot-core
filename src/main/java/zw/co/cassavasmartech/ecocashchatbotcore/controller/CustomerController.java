package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;
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

    @GetMapping("/{id}")
    public  EntityModel<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id).orElseThrow(() -> new CustomerNotFoundException(id.toString()));
        return assembler.toModel(customer);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseEntity<EntityModel<Customer>>> newCustomer(@Valid @RequestBody Customer customer) throws NotFoundException {
        Customer newCustomer = customerService.save(customer);
        ApiResponse<ResponseEntity<EntityModel<Customer>>> response = new ApiResponse<>();
        ResponseEntity<EntityModel<Customer>> entity = ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).getCustomerByCustomerChatId(newCustomer.getProfiles().get(0).getChatId())).toUri())
                .body(assembler.toModel(customer));
        response.setStatus(HttpStatus.OK.value());
        response.setBody(entity);
        response.setMessage("Success");
        return response;
    }

    @GetMapping("/chat/{id}")
    public ApiResponse<EntityModel<Customer>> getCustomerByCustomerChatId(@PathVariable String id) {
        Customer customer = customerService.findByCustomerChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        ApiResponse<EntityModel<Customer>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setBody(assembler.toModel(customer));
        return response;

    }

    @GetMapping("/list")
    public CollectionModel<EntityModel<Customer>> allCustomers() {
        List<EntityModel<Customer>> customers = customerService.findAll();
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel());
    }

    @GetMapping("/enrolled/{id}")
    public ResponseEntity isEnrolled(@PathVariable String id)
    {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        if(customerService.isEnrolled(id)) responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/question/{id}")
    public CollectionModel<Answer> getAnswer(@PathVariable String id){
        return CollectionModel.of(customerService.getAnswers(id),linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel()) ;
    }

    @GetMapping("/alternative/{id}")
    public ResponseEntity<?> getAlternative(@PathVariable String id){
        Customer customer = customerService.findByCustomerChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).toUri())
                .body(customerService.getAlternative(id));
    }

    @GetMapping("/statement/{id}")
    public ApiResponse<String> getStatement(@PathVariable String id){
        return customerService.getStatement(id);
    }

    @GetMapping("/pinreset/{id}")
    public ResponseEntity pinreset(@PathVariable String id){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        if(customerService.generateOtp(id)) responseEntity = new ResponseEntity(HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/otp/generate/{id}")
    public ResponseEntity generateOTP(@PathVariable String id){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        if(customerService.generateOtp(id)) responseEntity = new ResponseEntity(HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/otp/{otp}/verify/{id}")
    public ResponseEntity verifyCustomer(@PathVariable String id, @PathVariable String otp){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        if(customerService.verifyCustomer(id, otp)) responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> updateActiveCustomers(@PathVariable String id, @Valid @RequestBody Customer customer) {
            Customer myCustomer = customerService.update(id, customer).orElseThrow(()->new CustomerNotFoundException(id));
            return ResponseEntity
                    .created(linkTo(methodOn(CustomerController.class).getCustomerById(myCustomer.getId())).toUri())
                    .body(assembler.toModel(myCustomer));
        }



}
