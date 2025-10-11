package be.glennclaes.easyinvoice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.UUID;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // NIEUW: Gebruik een roulerende index en een set kleine symbolen
    private final String[] uniqueMarkers = {" ~", " =", " +", " -"};
    private int markerIndex = 0;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);

        String process = templateEngine.process("welcome-email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        mimeMessage.addHeader("X-Mail-Unique", UUID.randomUUID().toString());

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);

        helper.setSubject("Welcome to EasyInvoice! 🎉");
        helper.setText(process, true);

        mimeMessage.removeHeader("In-Reply-To");
        mimeMessage.removeHeader("References");
        mimeMessage.saveChanges();
        mailSender.send(mimeMessage);
    }

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp_code", otp);

        String process = templateEngine.process("verify-email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessage.addHeader("X-Mail-Unique", UUID.randomUUID().toString());

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);

        helper.setSubject("Verify Your Email Address");
        helper.setText(process, true);

        mimeMessage.removeHeader("In-Reply-To");
        mimeMessage.removeHeader("References");
        mimeMessage.saveChanges();
        mailSender.send(mimeMessage);
    }

    public void sendResetOtpEmail(String toEmail, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp_code", otp);

        String process = templateEngine.process("password-reset-email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


        mimeMessage.addHeader("X-Mail-Unique", UUID.randomUUID().toString());

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);

        helper.setSubject("Password Reset Request");
        helper.setText(process, true);

        mimeMessage.removeHeader("In-Reply-To");
        mimeMessage.removeHeader("References");
        mimeMessage.saveChanges();
        mailSender.send(mimeMessage);
    }
}