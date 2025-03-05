import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.CommentDaoImpl;
import config.TestConfig;
import constants.Constants;
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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CommentHibernateDaoTest {
    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    private User user;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        user = Constants.getInsertedUser();
    }

    @Rollback
    @Test
    public void createComment(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,Constants.COMMENTS_TABLE);
        commentDao.createComment(Constants.TO_INSERT_COMMENT_REVIEW_ID,Constants.TO_INSERT_COMMENT_DESCRIPTION,user);
        List<Comment> commentList = commentDao.getComments(Constants.TO_INSERT_COMMENT_REVIEW_ID,Constants.TO_INSERT_COMMENT_USER_ID,Constants.PAGE_SIZE,0);
        Assert.assertFalse(commentList.isEmpty());
        Assert.assertEquals(Constants.INSERTED_COMMENT_USER_ID,commentList.get(0).getUserId());
    }

    @Rollback
    @Test
    public void getCommentsFromReview(){
        List<Comment> commentList = commentDao.getComments(Constants.INSERTED_COMMENT_REVIEW_ID,Constants.INSERTED_COMMENT_USER_ID,Constants.PAGE_SIZE,0);
        Assert.assertFalse(commentList.isEmpty());
        Assert.assertEquals(Constants.TO_INSERT_COMMENT_USER_ID,commentList.get(0).getCommentId());
    }

}
