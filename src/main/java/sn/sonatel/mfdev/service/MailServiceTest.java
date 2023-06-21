package sn.sonatel.mfdev.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Stagiaire;

@Service
@AllArgsConstructor
public class MailServiceTest {

    private final TemplateEngine templateEngine;

    @Async
    public void sendmail(String mailFrom, String password, String mailTo) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
            props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailFrom, password);
                }
            }
        );
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailFrom, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        msg.setSubject("ACCORD DE STAGE");
        msg.setContent("ACCORD DE STAGE", "text/html");
        msg.setSentDate(new Date());
        String message =
            "*************************** <br>\n" +
            "\n" +
            "Sonatel:Scan antiviral effectue (1)<br> \n" +
            "\n" +
            "Courriel externe : Ce message provient de l'extérieur de SONATEL. Evitez de cliquer sur tout lien ou pièce jointe à moins d’en reconnaître l’expéditeur !<br>\n" +
            "\n" +
            "***************************<br>\n" +
            "Bonjour Mouhamed,<br>\n" +
            "\n" +
            "Faisant suite à votre demande de stage, nous avons le plaisir de vous informer que votre candidature a été retenue.<br>\n" +
            "\n" +
            "Il s’agira d’un stage pédagogique de 03 mois à partir du mois de Décembre.<br>\n" +
            "\n" +
            "<b>Merci de remplir le formulaire ci-joint pour confirmer votre disponibilité et de nous envoyer par la même occasion votre certificat d’inscription de l’année en cours.<br></b>\n" +
            "\n" +
            "En pièce jointe également la fiche de renseignement qui vous permettra de vous informer sur les modalités de votre stage\n" +
            "\n" +
            " ";
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        //        MimeBodyPart attachPart = new MimeBodyPart();

        //        attachPart.attachFile("/var/tmp/ucad.png");
        //        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    @Async
    public void sendSimpleMail(String mailFrom, String password, String object, String template, String mailTo, Map<String, Object> data)
        throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        //        props.put("mail.mime.charset", "UTF-8");
        //        props.put("mail.mime.charset","")

        Session session = Session.getInstance(
            props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailFrom, password);
                }
            }
        );
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailFrom, false));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        message.setSubject(object);

        Context context = new Context();
        context.setVariables(data);

        String html = templateEngine.process(template, context);
        message.setContent(html, "text/html");

        Transport.send(message);
    }
}
