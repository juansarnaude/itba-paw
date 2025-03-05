package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportDao reportDao;

    @Transactional(readOnly = true)
    @Override
    public int getTotalReports() {
        return reportDao.getTotalReports();
    }

    @Transactional(readOnly = true)
    @Override
    public int getTypeReports(int type) {
        return reportDao.getTypeReports(type);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object> getReports(String contentType) {
        List<Object> reports = new ArrayList<>();

        if (contentType == null || contentType.isEmpty()) {
            // If no content type specified, fetch all reports
            reports.addAll(reportDao.getCommentReports());
            reports.addAll(reportDao.getReviewReports());
            reports.addAll(reportDao.getMoovieListReports());
            reports.addAll(reportDao.getMoovieListReviewReports());
        } else {
            // Fetch reports based on the specified content type
            switch (contentType.toLowerCase()) {
                case "comment":
                    reports.addAll(reportDao.getCommentReports());
                    break;
                case "review":
                    reports.addAll(reportDao.getReviewReports());
                    break;
                case "moovielist":
                    reports.addAll(reportDao.getMoovieListReports());
                    break;
                case "moovielistreview":
                    reports.addAll(reportDao.getMoovieListReviewReports());
                    break;
                default:
                    // Return empty list for invalid content types
                    break;
            }
        }

        return reports;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewReport> getReviewReports() {

        return reportDao.getReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getReportedReviews() {

        return reportDao.getReportedReviews();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedReviewsCount() {
        return reportDao.getReportedReviewsCount();
    }

    @Transactional
    @Override
    public ReviewReport reportReview(int reviewId, int userId, int type) {
        LOGGER.info("reportReview insert");
        return reportDao.reportReview(reviewId, userId, type,null);
    }

    @Transactional
    @Override
    public void resolveReviewReport(int reviewId) {

        reportDao.resolveReviewReport(reviewId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        return reportDao.getMoovieListReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getReportedMoovieListReviews() {

        return reportDao.getReportedMoovieListReviews();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListReviewsCount() {
        return reportDao.getReportedMoovieListReviewsCount();
    }

    @Transactional
    @Override
    public MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type) {
        return reportDao.reportMoovieListReview(moovieListReviewId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveMoovieListReviewReport(int mlrId) {

        reportDao.resolveMoovieListReviewReport(mlrId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReport> getMoovieListReports() {

        return reportDao.getMoovieListReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieList> getReportedMoovieLists() {
        return reportDao.getReportedMoovieLists();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListsCount() {
        return reportDao.getReportedMoovieListsCount();
    }

    @Transactional
    @Override
    public MoovieListReport reportMoovieList(int moovieListId, int userId, int type) {
        return reportDao.reportMoovieList(moovieListId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveMoovieListReport(int mlId) {

        reportDao.resolveMoovieListReport(mlId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentReport> getCommentReports() {

        return reportDao.getCommentReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getReportedComments() {
        return reportDao.getReportedComments();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedCommentsCount() {
        return reportDao.getReportedCommentsCount();
    }

    @Transactional
    @Override
    public CommentReport reportComment(int commentId, int userId, int type) {
        return reportDao.reportComment(commentId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveCommentReport(int commentId) {

        reportDao.resolveCommentReport(commentId);
    }
}
