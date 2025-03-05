package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;

import java.util.Locale;
import java.util.Map;

public interface EmailService {
    void sendDeletedReviewMail(User user, Media media, Locale locale);

    void sendDeletedListMail(User user, MoovieList moovieList, Locale locale);

    void sendBannedUserMail(User userBanned, User userBanning, String banReason, Locale locale);

    void sendUnbannedUserMail(User userBanned, Locale locale);

    void sendMediaAddedToFollowedListMail(User user, MoovieList moovieList, Locale locale);

    void sendNotificationLikeMilestoneMoovieListMail(User user, int likeCount, MoovieList moovieList, Locale locale);

    void sendNotificationFollowMilestoneMoovieListMail(User user, int followCount, MoovieList moovieList, Locale locale);

    void sendVerificationEmail(User user, String token, Locale locale);

    void sendResetPasswordEmail(User user, String token, Locale locale);
}
