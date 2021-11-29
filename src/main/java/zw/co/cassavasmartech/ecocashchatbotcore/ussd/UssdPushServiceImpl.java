package zw.co.cassavasmartech.ecocashchatbotcore.ussd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.ValidateCustomerRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TicketStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;
import zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.UssdPushRequest;

import java.net.URI;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildEIPJsonHttpHeaders;
import static zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.Flares.FREE_FLOW;
import static zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.Flares.FREE_FLOW_FB;

@Component
@RequiredArgsConstructor
@Slf4j
public class UssdPushServiceImpl implements UssdPushService {
    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;
    @Autowired
    TicketService ticketService;
    @Autowired
    UssdPushServiceConfigurationProperties ussdPushServiceConfigurationProperties;
    @Autowired
    MobileNumberFormater mobileNumberFormater;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public Boolean sendPrompt(UssdPushRequest request) {
        log.debug("Processing ussd push transaction request {} to url {}", request, ussdPushServiceConfigurationProperties.getUrl());
        final URI uri = UriComponentsBuilder.fromHttpUrl(ussdPushServiceConfigurationProperties.getUrl()).buildAndExpand().toUri();
        final RequestEntity<UssdPushRequest> requestEntity = new RequestEntity<>(request, buildEIPJsonHttpHeaders(), HttpMethod.POST, uri);
        final ResponseEntity<?> responseEntity = restTemplate.exchange(requestEntity,String.class);
        if(responseEntity.getStatusCode().value()==200)
        return true;
        else return false;
    }

    @Override
    public ResponseEntity<String> handleCallback(String input, String msisdn) {
        String responseMessage="Incorrect PIN provided";
        TransactionResponse response = paymentGatewayProcessor.validateCustomer(ValidateCustomerRequest.builder().msisdn(mobileNumberFormater.formatMsisdnMinimum(msisdn)).pin(input).build());
        if(response.getField1().equalsIgnoreCase("200")) {
            responseMessage = "PIN validation successful";
            String reference = msisdn;
            Ticket ticket = ticketService.findByReferenceAndTicketStatus(reference, TicketStatus.OPEN);
            DialogFlowUtil.approveTransactionReversal(ticket);
        }
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(FREE_FLOW,FREE_FLOW_FB);
        return new ResponseEntity<>(responseMessage,responseHeaders, HttpStatus.OK);
    }
}
