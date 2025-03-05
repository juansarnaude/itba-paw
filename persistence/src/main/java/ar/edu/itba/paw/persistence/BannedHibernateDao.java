package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.User;
import org.postgresql.core.NativeQuery;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
public class BannedHibernateDao implements BannedDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BannedMessage> getBannedMessage(int userId) {
        final TypedQuery<BannedMessage> query = entityManager.createQuery("SELECT b FROM BannedMessage b LEFT JOIN User u ON u.id = b.modUserId WHERE b.bannedUserId = :userId", BannedMessage.class)
                .setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void createBannedMessage(int bannedUserId, int modUserId, String message, String modUsername) {
        try{
            entityManager.createNativeQuery("INSERT INTO bannedmessage (moduserid,bannedUserId,message, modusername) VALUES (:modUserId,:bannedUserId,:message, :modUsername)")
                    .setParameter("modUserId",modUserId)
                    .setParameter("bannedUserId",bannedUserId)
                    .setParameter("message",message)
                    .setParameter("modUsername", modUsername)
                    .executeUpdate();
        } catch (Exception e){
            throw new UnableToInsertIntoDatabase("BannedMessage creation failed, user already has a banned message");
        }
    }

    @Override
    public void deleteBannedMessage(int bannedUserId) {
        entityManager.createNativeQuery("DELETE FROM bannedMessage WHERE bannedUserid = :bannedUserId")
        .setParameter("bannedUserId",bannedUserId)
                .executeUpdate();
    }

    @Override
    public List<User> getBannedUsers() {
        return entityManager.createQuery(
                "SELECT u FROM User u WHERE u.role = -2"
                , User.class).getResultList();
    }

    @Override
    public int getBannedCount() {
            return getBannedUsers().size();
    }


}
