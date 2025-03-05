package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Image;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> listAll(int page);

    List<User> listAll(int role, int page);

    User createUser(String username, String email, String password);

    User createUserFromUnregistered(String username, String email, String password);

    void confirmRegister(int userId, int authenticated);

    Optional<User> findUserById(int userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber);

    int getSearchCount(String username);

    int getLikedMoovieListCountForUser(String username);

    int getUserCount();


    Optional<Profile> getProfileByUsername(String username);

    List<Profile> getMilkyPointsLeaders(int size, int pageNumber);

    void setProfilePicture(int userId, byte[] image);

    void updateProfilePicture(int userId, byte[] image);

    Optional<Image> getProfilePicture(int id);

    boolean hasProfilePicture(int userId);


    void changeUserRole(int userId, int role);
}
