package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenService verificationTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    final static int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    //REGSITRATION

    @Transactional(readOnly = true)
    @Override
    public List<User> listAll(int page) {
        return userDao.listAll(page);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listAll(int role, int page) {
        return userDao.listAll(role, page);
    }

    @Transactional
    @Override
    public String createUser(String username, String email, String password) {
        if (userDao.findUserByUsername(username).isPresent()) {
            LOGGER.info("Failed to created user with username: {}", username);
            throw new UnableToCreateUserException("username_taken");
        }
        Optional<User> aux = userDao.findUserByEmail(email);

        if (aux.isPresent()) {
            if (aux.get().getRole() == UserRoles.UNREGISTERED.getRole()) {
                User user = createUserFromUnregistered(username, email, password);
                String token = verificationTokenService.createVerificationToken(user.getUserId());
                emailService.sendVerificationEmail(user, token, LocaleContextHolder.getLocale());
                LOGGER.info("Succesfuly created user with username: {}", username);
                return token;
            } else {
                throw new UnableToCreateUserException("email_taken");
            }
        }
        try {
            User user = userDao.createUser(username, email, passwordEncoder.encode(password));
            LOGGER.info("Succesfuly created user with username: {}", username);
            String token = verificationTokenService.createVerificationToken(user.getUserId());
            emailService.sendVerificationEmail(user, token, LocaleContextHolder.getLocale());
            return token;
        } catch (Exception e) {
            throw new UnableToCreateUserException("Unable to create user");
        }
    }

    @Transactional
    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        try {
            return userDao.createUserFromUnregistered(username, email, passwordEncoder.encode(password));
        } catch (Exception e) {
            throw new UnableToCreateUserException("Unable to create user");
        }

    }

    @Transactional
    @Override
    public boolean confirmRegister(Token token) {
        boolean isValidToken = verificationTokenService.isValidToken(token);
        if (isValidToken) {
            int userId = token.getUserId();
            int role = findUserById(userId).getRole();

            if (role == UserRoles.UNREGISTERED.getRole() || role == UserRoles.NOT_AUTHENTICATED.getRole()) {
                userDao.confirmRegister(token.getUserId(), UserRoles.USER.getRole());
            } else if (role == UserRoles.MODERATOR_NOT_REGISTERED.getRole()) {
                userDao.confirmRegister(token.getUserId(), UserRoles.MODERATOR.getRole());
            } else if (role == UserRoles.BANNED_NOT_REGISTERED.getRole()) {
                userDao.confirmRegister(token.getUserId(), UserRoles.BANNED.getRole());
            }

            verificationTokenService.deleteToken(token);
        }
        moovieListDao.createMoovieList(token.getUserId(), "Watched", MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), "");
        moovieListDao.createMoovieList(token.getUserId(), "Watchlist", MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), "");

        LOGGER.info("Succesfuly confirmed user with userid: {}", token.getUserId());
        return isValidToken;
    }

    @Transactional
    @Override
    public String forgotPassword(User user) {
        String token = verificationTokenService.createVerificationToken(user.getUserId());
        if (verificationTokenService.getToken(token).isPresent()) {
            emailService.sendResetPasswordEmail(user, token, LocaleContextHolder.getLocale());
        }
        return token;
    }

    @Transactional
    @Override
    public boolean resetPassword(Token token, String newPassword) {
        boolean isValidToken = verificationTokenService.isValidToken(token);
        if (isValidToken) {
            findUserById(token.getUserId()).setPassword(passwordEncoder.encode(newPassword));
            verificationTokenService.deleteToken(token);
        }
        return isValidToken;
    }

    //FIND USERS


    @Transactional(readOnly = true)
    @Override
    public User findUserById(int userId) {
        return userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email).orElseThrow(() -> new UnableToFindUserException("User with email: " + email + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByUsername(String username) throws UnableToFindUserException {
        return userDao.findUserByUsername(username).orElseThrow(() -> new UnableToFindUserException("User with username: " + username + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber) {
        return userDao.searchUsers(username, setOrderBy(orderBy), setSortOrder(sortOrder), size, pageNumber - 1);
    }

    @Transactional(readOnly = true)
    @Override
    public int getSearchCount(String username) {
        return userDao.getSearchCount(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int getLikedMoovieListCountForUser(String username) {
        return userDao.getLikedMoovieListCountForUser(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int getUserCount() {
        return userDao.getUserCount();
    }

    @Transactional(readOnly = true)
    @Override
    public Profile getProfileByUsername(String username) {
        return userDao.getProfileByUsername(username).orElseThrow(() -> new UnableToFindUserException("No user with username: " + username));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Profile> getMilkyPointsLeaders(int size, int pageNumber) {
        return userDao.getMilkyPointsLeaders(size, pageNumber - 1);
    }

    //AUTHENTICATION INFO

    @Override
    public User getInfoOfMyUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new UserNotLoggedException("Security context is null");
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new UserNotLoggedException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new UserNotLoggedException("Unknown principal type");
        }

        // Fetch and return the user by username
        try {
            return findUserByUsername(username);
        } catch (UnableToFindUserException e) {
            throw new UserNotLoggedException("User not found");
        }
    }

    @Override
    public int tryToGetCurrentUserId() {
        try {
            return getInfoOfMyUser().getUserId();
        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return -1;
        }
    }

    @Override
    public boolean isUsernameMe(String username) {
        if (getInfoOfMyUser().getUsername().equals(username)) {
            return true;
        }
        throw new InvalidAccessToResourceException("Username provided doesnt match with current user.");
    }


    //PROFILE PICTURES


    @Transactional
    @Override
    public void setProfilePicture(byte[] image, String extension) {
        int uid = getInfoOfMyUser().getUserId();

        if (image.length > 0) {
            if (image.length > MAX_IMAGE_SIZE) {
                throw new InvalidTypeException("File is too big (Max is 5MB).");
            }

            if (extension != null || extension.equals("png") || extension.equals("jpg")
                    || extension.equals("jpeg") || extension.equals("gif")) {
                if (userDao.hasProfilePicture(uid)) {
                    userDao.updateProfilePicture(getInfoOfMyUser().getUserId(), image);
                    return;
                }
                userDao.setProfilePicture(getInfoOfMyUser().getUserId(), image);
            } else {
                throw new InvalidTypeException("File is not of type image");
            }
        } else {
            throw new NoFileException("No file was selected");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getProfilePicture(String username) {
        return userDao.getProfilePicture(findUserByUsername(username).getUserId()).orElseThrow(() -> new NoFileException("Profile picture not found")).getImage();
    }

    @Async
    @Transactional(readOnly = true)
    @Override
    public void resendVerificationEmail(Token token) {
        verificationTokenService.renewToken(token);
        User toRenewTokenUser = userDao.findUserById(token.getUserId()).orElseThrow(UnableToFindUserException::new);
        emailService.sendVerificationEmail(toRenewTokenUser, token.getToken(), LocaleContextHolder.getLocale());
    }

    private String setSortOrder(String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return "desc";
        }
        sortOrder = sortOrder.replaceAll(" ", "");
        if (sortOrder.toLowerCase().equals("asc")) {
            return "asc";
        }
        if (sortOrder.toLowerCase().equals("desc")) {
            return "desc";
        }
        return null;
    }

    private String setOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isEmpty()) {
            return "milkyPoints";
        }
        orderBy = orderBy.replaceAll(" ", "");
        if (orderBy.toLowerCase().equals("milkypoints")) {
            return "milkyPoints";
        }
        if (orderBy.toLowerCase().equals("userid")) {
            return "userId";
        }
        if (orderBy.toLowerCase().equals("username")) {
            return "username";
        }
        return null;
    }
}

