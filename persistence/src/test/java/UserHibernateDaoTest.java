import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserHibernateDao;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Optional;

import static constants.Constants.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserHibernateDaoTest {
    @Autowired
    private UserHibernateDao userHibernateDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testGetUserById(){
        final Optional<User> user = userHibernateDao.findUserById(INSERTED_USER_ID);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_EMAIL,user.get().getEmail());
    }

    @Rollback
    @Test
    public void testGetUserByEmail(){
        final Optional<User> user = userHibernateDao.findUserByEmail(INSERTED_USER_EMAIL);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_ID,user.get().getUserId());
    }

    @Rollback
    @Test
    public void testGetUserByUsername(){
        final Optional<User> user = userHibernateDao.findUserByUsername(INSERTED_USER_USERNAME);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_ID,user.get().getUserId());
    }

    @Rollback
    @Test
    public void testCreateUser() {
        final User user = userHibernateDao.createUser(TO_INSERT_USER_USERNAME,TO_INSERT_USER_EMAIL,TO_INSERT_USER_PASSWORD);
        entityManager.flush();

        Assert.assertNotNull(user);
        Assert.assertEquals(TO_INSERT_USER_EMAIL, user.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("email = '%s'", TO_INSERT_USER_EMAIL)));
    }

    @Rollback
    @Test
    public void testChangeUserRole(){
        userHibernateDao.changeUserRole(INSERTED_USER_ID,1);
        entityManager.flush();
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("role = '%d'", 1)));
    }

    @Rollback
    @Test
    public void testGetUserCount(){
        final int count = userHibernateDao.getUserCount();
        Assert.assertEquals(3,count);
    }

    @Rollback
    @Test
    public void testNonExistentUser(){
        final Optional<User> user = userHibernateDao.findUserById(NON_EXISTENT_USER_ID);
        Assert.assertFalse(user.isPresent());
    }
}
