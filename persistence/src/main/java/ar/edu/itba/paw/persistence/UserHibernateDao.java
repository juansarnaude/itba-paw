package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.util.*;

@Primary
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int INITIAL_MILKY_POINTS = 0;

    @Override
    public List<User> listAll(int page) {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public List<User> listAll(int role, int page) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", role)
                .getResultList();
    }

    //Revisar, está mal así
    @Override
    public User createUser(String username, String email, String password) {
        final User toCreateUser = new User.Builder(username, email, password, UserRoles.NOT_AUTHENTICATED.getRole(), INITIAL_MILKY_POINTS).build();
        entityManager.persist(toCreateUser);
        return toCreateUser;
    }

    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        entityManager.createNativeQuery("UPDATE users SET username = :username, password = :password, role = :role WHERE email = :email")
                .setParameter("username", username)
                .setParameter("password", password)
                .setParameter("role", UserRoles.NOT_AUTHENTICATED.getRole())
                .setParameter("email", email)
                .executeUpdate();


        return findUserByEmail(email).orElseThrow(() -> new UnableToFindUserException("Unable to find user"));
    }

    @Override
    public void confirmRegister(int userId, int authenticated) {
        User user = entityManager.find(User.class, userId);
        user.setRole(authenticated);
        entityManager.merge(user);
    }

    @Override
    public Optional<User> findUserById(int userId) {
        final TypedQuery<User> query = entityManager.createQuery("from User where userId = :userId", User.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        final TypedQuery<User> query = entityManager.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        final TypedQuery<User> query = entityManager.createQuery("from User where username = (:username)", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber) {
        final TypedQuery<Profile> query = entityManager.createQuery("FROM Profile WHERE LOWER(username) LIKE :username ORDER BY " + orderBy + " " + sortOrder, Profile.class);
        query.setParameter("username", '%' + username.toLowerCase() + '%').setFirstResult(pageNumber * size).setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public int getSearchCount(String username) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) AS count FROM users WHERE username ILIKE :username")
                .setParameter("username", '%' + username + '%')
                .getSingleResult()).intValue();
    }

    @Override
    public int getLikedMoovieListCountForUser(String username) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) AS count FROM moovieListsLikes mll LEFT JOIN users u ON mll.userid = u.userid WHERE username ILIKE :username")
                .setParameter("username", '%' + username + '%')
                .getSingleResult()).intValue();
    }

    @Override
    public int getUserCount() {
        return ((Number) entityManager.createQuery("SELECT COUNT(*) FROM User").getSingleResult()).intValue();
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        final TypedQuery<Profile> query = entityManager.createQuery("FROM Profile where LOWER(username) LIKE :username", Profile.class);
        query.setParameter("username", username.toLowerCase());
        return query.getResultList().stream().findFirst();
    }


    @Override
    public void setProfilePicture(int userId, byte[] image) {
        final Image toInsertImage = new Image(userId, image);
        entityManager.persist(toInsertImage);
    }

    @Override
    public boolean hasProfilePicture(int userId) {
        int aux = entityManager.createQuery("FROM Image WHERE userId = :userId")
                .setParameter("userId", userId)
                .getResultList().size();
        if (aux >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public void updateProfilePicture(int userId, byte[] image) {
        entityManager.createNativeQuery("UPDATE userImages SET image = :image WHERE userId = :userId")
                .setParameter("image", image)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public Optional<Image> getProfilePicture(int userId) {
        final TypedQuery<Image> query = entityManager.createQuery("from Image where userId = :userId", Image.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }


    /**
     * USER STATUS
     */

    @Override
    public void changeUserRole(int userId, int role) {
        entityManager.createNativeQuery("UPDATE users SET role = :role WHERE userId = :userId")
                .setParameter("role", role)
                .setParameter("userId", userId)
                .executeUpdate();
    }


    @Override
    public List<Profile> getMilkyPointsLeaders(int size, int pageNumber) {
        TypedQuery<Profile> query = entityManager.createQuery("FROM Profile p ORDER BY p.milkyPoints DESC", Profile.class);
        return query.setMaxResults(size).setFirstResult(pageNumber * size).getResultList();
    }

}
