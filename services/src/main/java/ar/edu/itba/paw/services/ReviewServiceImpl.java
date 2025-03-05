package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.ReviewAlreadyCreatedException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Transactional(readOnly = true)
    @Override
    public Review getReviewById(int reviewId) {
        return reviewDao.getReviewById( userService.tryToGetCurrentUserId(), reviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + reviewId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getReviewsByMediaId(int mediaId, int size, int pageNumber)  {
        List<Review> toReturn = reviewDao.getReviewsByMediaId(userService.tryToGetCurrentUserId(), mediaId, size, pageNumber);

        // porque reviews hacen su propio fetch de los comments
        // y el estado de watched es un Transient para cada comment que debe ser setteado en la creacion
        // hay que modificarlas en caso que el user este loggeado
        int currentUser = userService.tryToGetCurrentUserId();
        if (currentUser > 0){
            for(Review r : toReturn){
                for(Comment c : r.getComments()){
                    c.setCurrentUserHasLiked(commentService.userHasLiked(c.getCommentId(),currentUser));
                    c.setCurrentUserHasDisliked(commentService.userHasDisliked(c.getCommentId(),currentUser));
                }
            }
        }


        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public int getReviewsByMediaIdCount(int mediaId) {
        return reviewDao.getReviewsByMediaIdCount(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getMovieReviewsFromUser(int userId, int size, int pageNumber) {
        return reviewDao.getMovieReviewsFromUser(userService.tryToGetCurrentUserId(), userId, size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListReview getMoovieListReviewById(int moovieListReviewId) {
        return reviewDao.getMoovieListReviewById(userService.tryToGetCurrentUserId(), moovieListReviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + moovieListReviewId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getMoovieListReviewsByMoovieListId(int moovieListId, int size, int pageNumber) {
        return reviewDao.getMoovieListReviewsByMoovieListId(userService.tryToGetCurrentUserId(), moovieListId,size,pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getMoovieListReviewByMoovieListIdCount(int moovieListId) {
        return reviewDao.getMoovieListReviewByMoovieListIdCount(moovieListId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getMoovieListReviewsFromUser(int userId, int size, int pageNumber) {
        userService.findUserById(userId);
        return reviewDao.getMoovieListReviewsFromUser(userService.tryToGetCurrentUserId(), userId,size,pageNumber);
    }

    @Transactional
    @Override
    public boolean likeReview(int reviewId, ReviewTypes type) {
        if (type.getType()==ReviewTypes.REVIEW_MEDIA.getType()) {
            Review review = reviewDao.getReviewById(userService.tryToGetCurrentUserId(), reviewId).orElseThrow(() -> new ReviewNotFoundException("Review not found for id: " + reviewId));
            if(review.isCurrentUserHasLiked()){
                removeLikeReview(reviewId, type);
                return false;
            }
        }
        else {
            MoovieListReview review = reviewDao.getMoovieListReviewById(userService.tryToGetCurrentUserId(),reviewId).orElseThrow(() -> new ReviewNotFoundException("MoovieListReview not found for id: " + reviewId));
            if(review.isCurrentUserHasLiked()){
                removeLikeReview(reviewId, type);
                return false;
            }
        }

        reviewDao.likeReview(userService.getInfoOfMyUser().getUserId(),reviewId,type);
        LOGGER.info("Succesfully liked review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
        return true;
    }

    @Transactional
    @Override
    public void removeLikeReview(int reviewId, ReviewTypes type) {
        if (type.getType()==ReviewTypes.REVIEW_MEDIA.getType()) {
            Review review = reviewDao.getReviewById(userService.tryToGetCurrentUserId(), reviewId).orElseThrow(() -> new ReviewNotFoundException("Review not found for id: " + reviewId));
        }
        else {
            MoovieListReview review = reviewDao.getMoovieListReviewById(userService.tryToGetCurrentUserId(),reviewId).orElseThrow(() -> new ReviewNotFoundException("MoovieListReview not found for id: " + reviewId));
            }
        reviewDao.removeLikeReview(userService.getInfoOfMyUser().getUserId(),reviewId,type);
        LOGGER.info("Succesfully removed like in review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void createReview(int mediaId, int rating, String reviewContent, ReviewTypes type) {
        User user = userService.getInfoOfMyUser();

        if (type.getType() == ReviewTypes.REVIEW_MEDIA.getType() && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        if (type.getType() == ReviewTypes.REVIEW_MEDIA.getType() && reviewDao.getReviewByMediaIdAndUsername(mediaId, user.getUserId()) != null)
            throw new ReviewAlreadyCreatedException("Review already created in media with id: " + mediaId);
        else if (reviewDao.getMoovieListReviewByListIdAndUsername(mediaId, user.getUserId()) != null)
            throw new ReviewAlreadyCreatedException("Review already created in list with id: " + mediaId);

        reviewDao.createReview(user, mediaId, rating, reviewContent,type);
        LOGGER.info("Succesfully created review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());

    }

    @Transactional
    @Override
    public boolean editReview( int mediaId, int rating, String reviewContent, ReviewTypes type){
        int userId = userService.getInfoOfMyUser().getUserId();
        if (type.getType()==ReviewTypes.REVIEW_MEDIA.getType() && reviewDao.getReviewByMediaIdAndUsername(mediaId ,userId)==null){
            throw new ReviewNotFoundException("Review not found");
        }
        else if (type.getType()==ReviewTypes.REVIEW_MOOVIE_LIST.getType() && reviewDao.getMoovieListReviewByListIdAndUsername(mediaId ,userId)==null){
            throw new ReviewNotFoundException("MoovieList review not found");
        }
        boolean edited=reviewDao.editReview(userId, mediaId, rating, reviewContent,type);
        if (!edited){
            throw new NoSuchElementException("Review not found");
        }
        LOGGER.info("Succesfully edited review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());
        return true;
    }

    @Transactional
    @Override
    public void deleteReview(int reviewId, ReviewTypes type){
        User currentUser = userService.getInfoOfMyUser();
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()){
            Review review = reviewDao.getReviewById(currentUser.getUserId(), reviewId)
                    .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
            if (currentUser.getUserId()!=review.getUserId() && currentUser.getRole() != UserRoles.MODERATOR.getRole()) {
                throw new InvalidAccessToResourceException("User is not owner of the Review.");
            }
        } else{
            MoovieListReview review = reviewDao.getMoovieListReviewById(currentUser.getUserId(), reviewId)
                    .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
            if (currentUser.getUserId()!=review.getUserId() && currentUser.getRole() != UserRoles.MODERATOR.getRole()) {
                throw new InvalidAccessToResourceException("User is not owner of the Review.");
            }
        }

        reviewDao.deleteReview(reviewId,type);
        LOGGER.info("Succesfully deleted review: {}, user: {}.", reviewId , userService.getInfoOfMyUser().getUserId());
    }


    @Transactional(readOnly = true)
    @Override
    public Review getReviewByMediaIdAndUsername(int mediaId, int userId) {
        return reviewDao.getReviewByMediaIdAndUsername(mediaId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListReview getMoovieListReviewByListIdAndUsername(int listId, int userId) {
        return reviewDao.getMoovieListReviewByListIdAndUsername(listId, userId);
    }

}
