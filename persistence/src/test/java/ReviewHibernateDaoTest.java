import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ReviewHibernateDao;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewHibernateDaoTest {
    @Autowired
    private ReviewHibernateDao reviewHibernateDao;

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
    public void testGetReviewById(){
        final Optional<Review> review = reviewHibernateDao.getReviewById(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_ID);

        Assert.assertTrue(review.isPresent());
        Assert.assertEquals(Constants.INSERTED_REVIEW_RATING,review.get().getRating());
    }

    @Rollback
    @Test
    public void testGetReviewsByMediaId(){
        final List<Review> review = reviewHibernateDao.getReviewsByMediaId(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_MEDIA_ID,Constants.PAGE_SIZE,0);
        Assert.assertFalse(review.isEmpty());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, Constants.REVIEWS_TABLE, String.format("mediaid = '%d'", Constants.INSERTED_REVIEW_MEDIA_ID)));
    }

    @Rollback
    @Test
    public void testGetReviewsFromUser(){
        final List<Review> review = reviewHibernateDao.getMovieReviewsFromUser(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_USER_ID,Constants.PAGE_SIZE,0);
        Assert.assertFalse(review.isEmpty());
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, Constants.REVIEWS_TABLE, String.format("userid = '%d'", Constants.INSERTED_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testCreateReview(){
        reviewHibernateDao.createReview(user,Constants.TO_INSERT_REVIEW_MEDIA_ID,Constants.TO_INSERT_REVIEW_RATING_ID,Constants.TO_INSERT_REVIEW_DESCRIPTION,ReviewTypes.REVIEW_MEDIA);
        entityManager.flush();
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, Constants.REVIEWS_TABLE, String.format("userid = '%d'", Constants.TO_INSERT_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testDeleteReview(){
        reviewHibernateDao.deleteReview(Constants.INSERTED_REVIEW_USER_ID,ReviewTypes.REVIEW_MEDIA);
        entityManager.flush();
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, Constants.REVIEWS_TABLE, String.format("userid = '%d'", Constants.INSERTED_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testGetReviewsByMediaIdCount(){
        final int count = reviewHibernateDao.getReviewsByMediaIdCount(Constants.INSERTED_REVIEW_MEDIA_ID);
        Assert.assertEquals(1,count);
    }

    @Rollback
    @Test
    public void testEditReview(){
        Optional<Review> previousReview = reviewHibernateDao.getReviewById(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_ID);
        Assert.assertNotNull(previousReview);
        Assert.assertEquals(Constants.INSERTED_REVIEW_RATING,previousReview.get().getRating());
        Assert.assertEquals(Constants.INSERTED_REVIEW_DESCRIPTION,previousReview.get().getReviewContent());
        reviewHibernateDao.editReview(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_MEDIA_ID,Constants.TO_INSERT_REVIEW_RATING_ID,Constants.TO_INSERT_REVIEW_DESCRIPTION,ReviewTypes.REVIEW_MEDIA);
        entityManager.flush();
        Optional<Review> editedReview = reviewHibernateDao.getReviewById(Constants.INSERTED_REVIEW_USER_ID,Constants.INSERTED_REVIEW_ID);
        Assert.assertNotNull(editedReview);
        Assert.assertEquals(Constants.TO_INSERT_REVIEW_RATING_ID,editedReview.get().getRating());
        Assert.assertEquals(Constants.TO_INSERT_REVIEW_DESCRIPTION,editedReview.get().getReviewContent());
    }

}
