package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
import java.text.ParseException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
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
    public ApiResponse<EnrollmentResponse> isEnrolled(@PathVariable String chatId)
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

    @PostMapping("/security/question/answer/{chatId}")
    public ApiResponse<Boolean> verifyAnswer(@PathVariable String chatId, @Valid @RequestBody VerifyAnswerRequest verifyAnswerRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.verifyAnswers(chatId,verifyAnswerRequest));

    }

    @GetMapping("/alternative/{chatId}")
    public ApiResponse<?> getAlternative(@PathVariable String chatId){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.getAlternative(chatId));
    }

    @PostMapping("/statement/{chatId}")
    public ApiResponse<Statement> getStatement(@PathVariable String chatId, @Valid @RequestBody StatementRequest statementRequest) throws ParseException {
        ApiResponse<Statement> response = new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, customerService.getStatement(chatId, statementRequest));
        log.info("Statement response: {}", response);
        return response;
    }

    @GetMapping("/statement/downloadFile/{documentId}")
    public void downloadFile(@PathVariable("documentId") String documentId, HttpServletRequest req, HttpServletResponse resp) {
        customerService.getStatementFile(documentId, req, resp);
    }

    @GetMapping("/pinreset/{chatId}")
    public ApiResponse<Boolean> pinreset(@PathVariable String chatId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.pinReset(chatId));

    }

    @RequestMapping(value = "/pdf/{fileName:.+}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> download(@PathVariable("fileName") String fileName) throws IOException {
        System.out.println("Calling Download:- " + fileName);
        ClassPathResource pdfFile = new ClassPathResource("downloads/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.setContentLength(pdfFile.contentLength());
        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
                new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
        return response;

    }

    @PostMapping("/paybiller/{chatId}")
    public ApiResponse<TransactionResponse> payBiller(@PathVariable String chatId, @Valid @RequestBody SubscriberToBillerRequest subscriberToBillerRequest){
        log.info("Request from pay biller bot: {}", subscriberToBillerRequest.getBillerCode());
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.payBiller(chatId,subscriberToBillerRequest));
    }

    @PostMapping("/biller/lookup")
    public ApiResponse<TransactionResponse> billerLookup(@RequestBody BillerLookupRequest billerLookupRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.billerLookup(billerLookupRequest));
    }

    @PostMapping("/paymerchant/{chatId}")
    public ApiResponse<EipTransaction> payMerchant(@PathVariable String chatId, @Valid @RequestBody SubscriberToMerchantRequest subscriberToMerchantRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.payMerchant(chatId, subscriberToMerchantRequest));
    }

    @PostMapping("/paymerchant2/{chatId}")
    public ApiResponse<EipTransaction> payMerchant2(@PathVariable String chatId, @Valid @RequestBody SubscriberToMerchantRequest subscriberToMerchantRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.payMerchant2(chatId, subscriberToMerchantRequest));
    }

    @PostMapping("/airtime/{chatId}")
    public ApiResponse<TransactionResponse> buyAirtime(@PathVariable String chatId, @Valid @RequestBody SubscriberAirtimeRequest subscriberAirtimeRequest){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.buyAirtime(chatId,subscriberAirtimeRequest));
    }

    @PostMapping("/subscriber/lookup")
    public ApiResponse<TransactionResponse> lookupSubscriber(@Valid @RequestBody SubscriberDto subscriberDto){
        return new ApiResponse<>(HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.customerLookup(subscriberDto));
    }

    @PostMapping("/sendmoney/{chatId}")
    public ApiResponse<TransactionResponse> sendMoney(@PathVariable String chatId, @Valid @RequestBody SubscriberToSubscriberRequest subscriberToSubscriberRequest){
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.sendMoney(chatId, subscriberToSubscriberRequest)
        );
    }

    @PostMapping("/registration")
    public ApiResponse<TransactionResponse> registerCustomer(@Valid @RequestBody Registration registration){
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                customerService.registerCustomer(registration)

        );
    }



}
