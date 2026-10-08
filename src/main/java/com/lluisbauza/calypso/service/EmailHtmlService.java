package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationEmailData;
import com.lluisbauza.calypso.enums.ReservationStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailHtmlService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailHtmlService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailWithHtml(String to, String subject, ReservationEmailData data) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("reservation", data);

            String html;

            if (data.status().equals(ReservationStatus.CANCELLED)) {
                html = templateEngine.process("email/reservation-cancellation", context);
            } else {
                html = templateEngine.process("email/reservation-confirmation", context);
            }

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            System.out.println("Email could not be sent: " + e.getMessage());
        }
    }
}
