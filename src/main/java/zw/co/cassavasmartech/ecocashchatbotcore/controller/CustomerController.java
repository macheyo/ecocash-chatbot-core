package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/customers")
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
    public ResponseEntity<EntityModel<Customer>> newCustomer(@Valid @RequestBody Customer customer) throws NotFoundException {
        Customer newCustomer = customerService.save(customer);
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).getCustomerByCustomerChatId(newCustomer.getProfiles().get(0).getChatId())).toUri())
                .body(assembler.toModel(customer));
    }

    @GetMapping("/chat/{id}")
    public EntityModel<Customer> getCustomerByCustomerChatId(@PathVariable String id) {
        Customer customer = customerService.findByCustomerChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        return assembler.toModel(customer);
    }

    @GetMapping
    public CollectionModel<EntityModel<Customer>> allCustomers() {

        List<EntityModel<Customer>> customers = customerService.findAll();
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel());
    }

    @GetMapping("/otp/{id}")
    public ResponseEntity getOTP(@PathVariable String customerChatId){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.CREATED);
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
