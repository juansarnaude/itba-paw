import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ReportDaoImpl;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReportHibernateDaoTest {

    @Autowired
    private ReportDaoImpl reportDao;

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
    public void testReportReview(){
        //Create a review to report
        Assert.assertEquals(0,reportDao.getReportedReviews().size());
        reportDao.reportReview(Constants.INSERTED_REVIEW_ID,Constants.INSERTED_REVIEW_USER_ID, ReviewTypes.REVIEW_MEDIA.getType(),Constants.REPORT_CONTENT);
        //Check that the review was reported
        Assert.assertEquals(1,reportDao.getReportedReviews().size());
        List<ReviewReport> reviewReports = reportDao.getReviewReports();
        Assert.assertEquals(1,reviewReports.size());
        Assert.assertEquals(Constants.INSERTED_REVIEW_ID,reviewReports.get(0).getReview().getReviewId());
        Assert.assertEquals(Constants.INSERTED_REVIEW_USER_ID,reviewReports.get(0).getReportedBy().getUserId());
        Assert.assertEquals(Constants.REPORT_CONTENT,reviewReports.get(0).getContent());
        //Resolve the report
        reportDao.resolveReviewReport(Constants.INSERTED_REVIEW_ID);
        entityManager.flush();
        Assert.assertEquals(0,reportDao.getReportedReviews().size());
    }

    @Rollback
    @Test
    public void testReportComment(){
        //Create a comment to report
        Assert.assertEquals(0,reportDao.getReportedComments().size());
        reportDao.reportComment(Constants.INSERTED_COMMENT_ID, user.getUserId(), ReviewTypes.REVIEW_MEDIA.getType(),Constants.REPORT_CONTENT);
        //Check that the comment was reported
        Assert.assertEquals(1,reportDao.getReportedComments().size());
        List<CommentReport> commentReports = reportDao.getCommentReports();
        Assert.assertEquals(1,commentReports.size());
        Assert.assertEquals(Constants.INSERTED_COMMENT_ID,commentReports.get(0).getComment().getCommentId());
        Assert.assertEquals(user.getUserId(),commentReports.get(0).getReportedBy().getUserId());
        Assert.assertEquals(Constants.REPORT_CONTENT,commentReports.get(0).getContent());
        //Resolve the report
        reportDao.resolveCommentReport(Constants.INSERTED_COMMENT_ID);
        entityManager.flush();
        Assert.assertEquals(0,reportDao.getReportedComments().size());
    }

    @Rollback
    @Test
    public void testReportMoovieList(){
        //Create a moovielist to report
        Assert.assertEquals(0,reportDao.getReportedMoovieLists().size());
        reportDao.reportMoovieList(Constants.INSERTED_MOOVIELIST_ID, user.getUserId(), ReviewTypes.REVIEW_MEDIA.getType(), Constants.REPORT_CONTENT);
        //Check that the moovielist was reported
        Assert.assertEquals(1,reportDao.getReportedMoovieLists().size());
        List<MoovieListReport> moovieListReports = reportDao.getMoovieListReports();
        Assert.assertEquals(1,moovieListReports.size());
        Assert.assertEquals(Constants.INSERTED_MOOVIELIST_ID,moovieListReports.get(0).getMoovieList().getMoovieListId());
        Assert.assertEquals(user.getUserId(),moovieListReports.get(0).getReportedBy().getUserId());
        Assert.assertEquals(Constants.REPORT_CONTENT,moovieListReports.get(0).getContent());
        //Resolve the report
        reportDao.resolveMoovieListReport(Constants.INSERTED_MOOVIELIST_ID);
        entityManager.flush();
        Assert.assertEquals(0,reportDao.getReportedMoovieLists().size());
    }
}
