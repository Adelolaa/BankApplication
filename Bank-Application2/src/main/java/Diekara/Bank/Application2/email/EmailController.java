package Diekara.Bank.Application2.email;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
@Tag(
        name = "Email account service REST APIs/Endpoint",
        description = "Endpoints for manipulating Email Account"
)

public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/sendMail")
    public String sendEmail(@RequestBody EmailDetail emailDetail){
        return emailService.sendSimpleEmail(emailDetail);
    }

    @PostMapping("/sendMailWithoutAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetail emailDetail){
        return emailService.sendEmailWithAttachment(emailDetail);
    }
}



