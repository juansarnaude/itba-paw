package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;

import java.util.List;

public interface ReviewService {
    Review getReviewById(int reviewId);
    //Get all the reviews for a media, contains all nescesary  data to display
    List<Review> getReviewsByMediaId(int mediaId, int size, int pageNumber) ;
    int getReviewsByMediaIdCount(int mediaId);
    //Returns all the reviews a user has made, contains all nescesary data to display
    List<Review> getMovieReviewsFromUser(int userId, int size, int pageNumber);

    MoovieListReview getMoovieListReviewById(int moovieListReviewId);
    List<MoovieListReview> getMoovieListReviewsByMoovieListId(int moovieListId, int size, int pageNumber) ;
    int getMoovieListReviewByMoovieListIdCount(int moovieListId);
    List<MoovieListReview> getMoovieListReviewsFromUser(int userId, int size, int pageNumber);


    //The following work for both MoovieListsReviews and Reviews
    void createReview(int mediaId, int rating, String reviewContent, ReviewTypes type);

    boolean editReview(int mediaId, int rating, String reviewContent, ReviewTypes type);
    void deleteReview(int reviewId, ReviewTypes type);

    boolean likeReview(int reviewId, ReviewTypes type);
    void removeLikeReview(int reviewId, ReviewTypes type);

    Review getReviewByMediaIdAndUsername(int mediaId, int userId);
    MoovieListReview getMoovieListReviewByListIdAndUsername(int listId, int userId);

}
