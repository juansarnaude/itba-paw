package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;

import java.util.List;

public interface ReportDao {
    // STATS

    int getTotalReports();

    int getTypeReports(int type);

    // (Media) Reviews

    List<ReviewReport> getReviewReports();

    List<Review> getReportedReviews();

    int getReportedReviewsCount();

    ReviewReport reportReview(int reviewId, int userId, int type, String content);

    void resolveReviewReport(int reviewId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();

    List<MoovieListReview> getReportedMoovieListReviews();

    int getReportedMoovieListReviewsCount();

    MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type, String content);

    void resolveMoovieListReviewReport(int mlrId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();

    List<MoovieList> getReportedMoovieLists();

    int getReportedMoovieListsCount();

    MoovieListReport reportMoovieList(int moovieListId, int userId, int type, String content);

    void resolveMoovieListReport(int mlId);

    // (Review) Comments

    List<CommentReport> getCommentReports();

    List<Comment> getReportedComments();

    int getReportedCommentsCount();

    CommentReport reportComment(int commentId, int userId, int type, String content);

    void resolveCommentReport(int commentId);


}
