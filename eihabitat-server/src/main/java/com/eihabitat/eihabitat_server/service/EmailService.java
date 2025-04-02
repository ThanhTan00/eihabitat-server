package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.entity.EmailConfirmationToken;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    private JavaMailSender sender;

    public void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException, jakarta.mail.MessagingException {
        //MIME - HTML message
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailConfirmationToken.getEmail());  // Use correct email field
            helper.setSubject("Confirm your E-Mail - EIHABITAT Registration");
            helper.setText("<html>" +
                    "<body>" +
                    "<h2>Dear " + emailConfirmationToken.getFirstName() +" "+emailConfirmationToken.getLastName()+",</h2>"
                    + "<br/> We're excited to have you get started. " +
                    "Please click on the link below to confirm your account."
                    + "<br/> " + generateConfirmationLink(emailConfirmationToken.getToken()) + "" +
                    "<br/> Regards,<br/>" +
                    "Eihabitat team" +
                    "</body>" +
                    "</html>", true);

            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();  // Log the exception to understand the error
            // You might want to handle the error properly (e.g., show a message to the user)
        }

        //sender.send(message);
    }

    private String generateConfirmationLink(String token){
        return "<a href=https://eihabitat.site/api/auth/confirm-email?token="+token+">Click Here To Confirm Your Email</a>";
    }
}
