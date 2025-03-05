package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.UriInfo;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    @Qualifier("basePath")
    private String basePath;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private MessageSource messageSource;


    private static final int MULTIPART_MODE = MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;
    private static final String ENCODING = StandardCharsets.UTF_8.name();
    private static final String FROM = "no-reply@moovie.com";

    private void sendEmail(String to, String subject, String template, Map<String, Object> variables, Locale locale) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE, ENCODING);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(FROM);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(getHtmlBody(template, variables, locale), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {

        }
    }

    private String getHtmlBody(String template, Map<String, Object> variables, Locale locale) {
        Context context = new Context(locale);
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("basePath", basePath);
        context.setVariables(variables);
        return springTemplateEngine.process(template, context);
    }

    @Async
    @Override
    public void sendDeletedReviewMail(User user, Media media, Locale locale) {
        final String subject = messageSource.getMessage("email.reviewDeletedSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("mediaName", media.getName());
        sendEmail(user.getEmail(), subject, "yourReviewHasBeenRemovedEmail.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendDeletedListMail(User user, MoovieList moovieList, Locale locale) {
        final String subject = messageSource.getMessage("email.listDeletedSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("moovieListName", moovieList.getName());
        sendEmail(user.getEmail(), subject, "yourListHasBeenRemovedMail.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendBannedUserMail(User userBanned, User userBanning, String banReason, Locale locale) {
        final String subject = messageSource.getMessage("email.bannedSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", userBanned.getUsername());
        mailMap.put("modUsername", userBanning.getUsername());
        mailMap.put("message", banReason);
        sendEmail(userBanned.getEmail(), subject, "youHaveBeenBannedMail.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendUnbannedUserMail(User userUnbanned, Locale locale) {
        final String subject = messageSource.getMessage("email.unbannedSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", userUnbanned.getUsername());
        sendEmail(userUnbanned.getEmail(), subject, "youHaveBeenUnbannedMail.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendMediaAddedToFollowedListMail(User user, MoovieList moovieList, Locale locale) {
        final String subject = messageSource.getMessage("email.newMediaAddedSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("moovieListId", moovieList.getMoovieListId());
        mailMap.put("moovieListName", moovieList.getName());
        sendEmail(user.getEmail(), subject, "mediaAddedToFollowedList.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendNotificationLikeMilestoneMoovieListMail(User user, int likeCount, MoovieList moovieList, Locale locale) {
        final String subject = messageSource.getMessage("email.notificationLikeMilestoneMoovieListSubject", null, locale);
        Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("likes", likeCount);
        mailMap.put("moovieListId", moovieList.getMoovieListId());
        mailMap.put("moovieListName", moovieList.getName());
        sendEmail(user.getEmail(), subject, "notificationLikeMilestoneMoovieList.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendNotificationFollowMilestoneMoovieListMail(User user, int followCount, MoovieList moovieList, Locale locale) {
        final String subject = messageSource.getMessage("email.notificationFollowMilestoneMoovieListSubject", null, locale);
        Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("follows", followCount);
        mailMap.put("moovieListId", moovieList.getMoovieListId());
        mailMap.put("moovieListName", moovieList.getName());
        sendEmail(user.getEmail(), subject, "notificationFollowMilestoneMoovieList.html", mailMap, locale);
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String token, Locale locale) {
        final String subject = messageSource.getMessage("email.confirmationSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("token", token);
        sendEmail(user.getEmail(), subject, "confirmationMail.html", mailMap, locale);
    }

    @Override
    public void sendResetPasswordEmail(User user, String token, Locale locale) {
        final String subject = messageSource.getMessage("email.resetPasswordSubject", null, locale);
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getUsername());
        mailMap.put("token", token);
        sendEmail(user.getEmail(), subject, "resetPassword.html", mailMap, locale);
    }
}
