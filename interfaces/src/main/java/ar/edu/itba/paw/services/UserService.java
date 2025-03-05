package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;

import java.util.List;

public interface UserService {

    //API
    List<User> listAll(int page);

    List<User> listAll(int role, int page);

    //Registration actions
    String createUser(String username, String email, String password);

    User createUserFromUnregistered(String username, String email, String password);

    boolean confirmRegister(Token token);

    String forgotPassword(User user);

    boolean resetPassword(Token token, String newPassword);

    //User finders
    User findUserById(int userId);

    User findUserByEmail(String mail);

    User findUserByUsername(String username) throws UnableToFindUserException;

    //Search user (recomeneded only for the searchbar)
    List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber);

    //Search user count for pagination in searchUsers cases
    int getSearchCount(String username);

    //Liked amount lof list for a user
    int getLikedMoovieListCountForUser(String username);

    //returns total user count
    int getUserCount();

    //Return the parameters needed to show in the profile page
    Profile getProfileByUsername(String username);

    //Returns a list of the users with most milkyPoints
    List<Profile> getMilkyPointsLeaders(int size, int pageNumber);

    //Auth info of users
    User getInfoOfMyUser();

    //Returns -1 if not authenticated
    int tryToGetCurrentUserId();

    boolean isUsernameMe(String username);

    //Profile picture functions
    void setProfilePicture(byte[] imageBytes, String extension);

    byte[] getProfilePicture(String username);

    //Verification mail methods
    void resendVerificationEmail(Token token);
}
