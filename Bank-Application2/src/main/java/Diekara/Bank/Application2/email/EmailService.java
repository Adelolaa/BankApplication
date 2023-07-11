package Diekara.Bank.Application2.email;

public interface EmailService {
    String sendSimpleEmail(EmailDetail emailDetail);
    String sendEmailWithAttachment(EmailDetail emailDetail);
}
