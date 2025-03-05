package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;

import java.util.List;

public interface ReportService {

    // STATS

    int getTotalReports();

    int getTypeReports(int type);

    //    GENERAL REPORTS
    List<Object> getReports(String contentType);


    // (Media) Reviews

    List<ReviewReport> getReviewReports();

    List<Review> getReportedReviews();

    int getReportedReviewsCount();

    ReviewReport reportReview(int reviewId, int userId, int type);

    void resolveReviewReport(int reviewId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();

    List<MoovieListReview> getReportedMoovieListReviews();

    int getReportedMoovieListReviewsCount();

    MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type);

    void resolveMoovieListReviewReport(int mlrId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();

    List<MoovieList> getReportedMoovieLists();

    int getReportedMoovieListsCount();

    MoovieListReport reportMoovieList(int moovieListId, int userId, int type);

    void resolveMoovieListReport(int mlId);

    // (Review) Comments

    List<CommentReport> getCommentReports();

    List<Comment> getReportedComments();

    int getReportedCommentsCount();

    CommentReport reportComment(int commentId, int userId, int type);

    void resolveCommentReport(int commentId);
}
