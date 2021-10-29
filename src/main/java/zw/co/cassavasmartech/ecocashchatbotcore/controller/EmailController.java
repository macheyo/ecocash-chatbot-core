package zw.co.cassavasmartech.ecocashchatbotcore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.cassavasmartech.ecocashchatbotcore.email.EmailService;
import zw.co.cassavasmartech.ecocashchatbotcore.email.data.EmailNotification;

@RestController
    @RequestMapping("/dialogflow/agent/mail")
    public class EmailController {
    @Autowired
    EmailService service;

        @PostMapping("/send")
        public ResponseEntity<String> sendMail(@RequestBody EmailNotification mail) {
            service.send(mail);
            return new ResponseEntity<>("Email Sent successfully", HttpStatus.OK);
        }


    }

