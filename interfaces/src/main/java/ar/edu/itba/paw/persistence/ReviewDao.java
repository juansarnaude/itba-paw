package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Optional<Review> getReviewById(int currentUserId, int reviewId);
    List<Review> getReviewsByMediaId(int currentUserId, int mediaId, int size, int pageNumber);
    List<Review> getMovieReviewsFromUser(int currentUserId, int userId, int size, int pageNumber);
    int getReviewsByMediaIdCount(int mediaId);

    Optional<MoovieListReview> getMoovieListReviewById(int currentUserId, int moovieListReviewId);
    List<MoovieListReview> getMoovieListReviewsByMoovieListId(int currentUserId, int moovieListId, int size, int pageNumber) ;
    int getMoovieListReviewByMoovieListIdCount(int moovieListId);
    List<MoovieListReview> getMoovieListReviewsFromUser(int currentUserId, int userId, int size, int pageNumber);


    void createReview(User user, int mediaId, int rating, String reviewContent, ReviewTypes type);

    boolean editReview(int userId, int mediaId, int rating, String reviewContent, ReviewTypes type);
    void deleteReview(int reviewId, ReviewTypes type);

    void likeReview(int userId, int reviewId, ReviewTypes type);
    void removeLikeReview(int userId, int reviewId, ReviewTypes type);
    Review getReviewByMediaIdAndUsername(int mediaId, int userId);
    MoovieListReview getMoovieListReviewByListIdAndUsername(int listId, int userId);

    }
