package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAuthenticationLevelRequiredToPerformActionException;
import ar.edu.itba.paw.exceptions.UnableToBanUserException;
import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.BannedDao;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.ReviewDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeratorServiceImpl implements ModeratorService {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MoovieListDao moovieListDao;
    @Autowired
    private MoovieListService moovieListService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BannedDao bannedDao;
    @Autowired

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Transactional
    @Override
    public void deleteReview(int reviewId, int mediaId, ReviewTypes type) {
        amIModerator();

        Media m = mediaService.getMediaById(mediaId);
        Review r = reviewService.getReviewById(reviewId);
        User u = userService.findUserById(r.getUserId());

        emailService.sendDeletedReviewMail(u, m, LocaleContextHolder.getLocale());
        reviewDao.deleteReview(reviewId, type);
        LOGGER.info("Succesfully removed review: {}. (by mod)", reviewId);
    }

    @Transactional
    @Override
    public void deleteMoovieListList(int moovieListId) {
        amIModerator();

        MoovieList m = moovieListService.getMoovieListById(moovieListId);
        User u = userService.findUserById(m.getUserId());

        emailService.sendDeletedListMail(u, m, LocaleContextHolder.getLocale());

        moovieListDao.deleteMoovieList(moovieListId);
        LOGGER.info("Succesfully removed list: {}. (by mod)", moovieListId);
    }

    @Transactional
    @Override
    public void banUser(int userId, String message) throws UnableToBanUserException, InvalidAuthenticationLevelRequiredToPerformActionException {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId));
        if (u.getRole() == UserRoles.MODERATOR.getRole() || u.getRole() == UserRoles.MODERATOR_NOT_REGISTERED.getRole()) {
            throw new UnableToBanUserException("Cannot ban another moderator");
        }

        if (u.getRole() == UserRoles.NOT_AUTHENTICATED.getRole()) {
            throw new UnableToBanUserException("Cannot ban an unantheuticated user.");
        }

        if (u.getRole() == UserRoles.UNREGISTERED.getRole()) {
            userDao.changeUserRole(userId, UserRoles.BANNED_NOT_REGISTERED.getRole());
        } else {
            userDao.changeUserRole(userId, UserRoles.BANNED.getRole());
        }
        User currentUser = userService.getInfoOfMyUser();
        bannedDao.createBannedMessage(userId, currentUser.getUserId(), message, currentUser.getUsername());
        LOGGER.info("Succesfully banned user: {}.", userId);

        emailService.sendBannedUserMail(u, userService.getInfoOfMyUser(), message, LocaleContextHolder.getLocale());
    }

    @Transactional
    @Override
    public void unbanUser(int userId) throws UnableToFindUserException, UnableToChangeRoleException, InvalidAuthenticationLevelRequiredToPerformActionException {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId));

        if (u.getRole() == UserRoles.UNREGISTERED.getRole() || u.getRole() == UserRoles.USER.getRole() || u.getRole() == UserRoles.NOT_AUTHENTICATED.getRole()) {
            throw new UnableToChangeRoleException("Cant unban if its not banned");
        }

        if (u.getRole() == UserRoles.BANNED_NOT_REGISTERED.getRole()) {
            userDao.changeUserRole(userId, UserRoles.UNREGISTERED.getRole());
        } else if (u.getRole() == UserRoles.BANNED.getRole()) {
            userDao.changeUserRole(userId, UserRoles.USER.getRole());
        }
        bannedDao.deleteBannedMessage(userId);

        LOGGER.info("Succesfully unbanned user: {}.", userId);

        emailService.sendUnbannedUserMail(u, LocaleContextHolder.getLocale());
    }


    @Transactional
    @Override
    public void makeUserModerator(int userId) {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId));

        if (u.getRole() == UserRoles.MODERATOR.getRole() || u.getRole() == UserRoles.MODERATOR_NOT_REGISTERED.getRole()) {
            throw new UnableToChangeRoleException("Unable to change role of uid: " + userId + ", user must not be ROLE_MODERATOR");
        }

        if (u.getRole() == UserRoles.UNREGISTERED.getRole()) {
            userDao.changeUserRole(userId, UserRoles.MODERATOR_NOT_REGISTERED.getRole());
            return;
        } else if (u.getRole() == UserRoles.USER.getRole()) {
            userDao.changeUserRole(userId, UserRoles.MODERATOR.getRole());
        }

        LOGGER.info("Succesfully made user: {} moderator.", userId);

    }

    private void amIModerator() throws InvalidAuthenticationLevelRequiredToPerformActionException {
        if (userService.getInfoOfMyUser().getRole() != UserRoles.MODERATOR.getRole()) {
            throw new InvalidAuthenticationLevelRequiredToPerformActionException("To perform this action you must have role: moderator");
        }
    }

    @Transactional
    @Override
    public void deleteListReview(int moovieListReviewId) {
        amIModerator();
        moovieListDao.deleteListReview(moovieListReviewId);
        LOGGER.info("Succesfully deleted report for review: {}.", moovieListReviewId);
    }

}
