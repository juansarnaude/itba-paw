package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("accessValidator")
public class AccessValidator {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MoovieListService listService;


    @Autowired
    private ReviewService reviewService;

    public boolean checkIsUserMe (String username) {
        User userByUsername=userService.findUserByUsername(username);
        User loggedUser=userService.getInfoOfMyUser();
        return userByUsername!=null && loggedUser!=null && userByUsername.getUserId()==loggedUser.getUserId();
    }

    public boolean isUserLoggedIn() {
        return userService.getInfoOfMyUser() != null;
    }

    public boolean isUserAdmin() {
        return userService.getInfoOfMyUser() != null && userService.getInfoOfMyUser().getRole()== UserRoles.MODERATOR.getRole();
    }

    public boolean isUserCommentAuthor(int commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            User currentUser = userService.getInfoOfMyUser();

            if (comment == null || currentUser == null) {
                return false;
            }

            return currentUser.getUserId() == comment.getUserId();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserListAuthor(int listId) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            MoovieList list = listService.getMoovieListById(listId);
            return currentUser != null && list!=null && currentUser.getUserId() == list.getUserId();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserMoovieListReviewAuthor(int reviewId) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            MoovieListReview review = reviewService.getMoovieListReviewById(reviewId);
            return currentUser != null && review != null && currentUser.getUserId() == review.getUserId();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserReviewAuthor(int reviewId) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            Review review = reviewService.getReviewById(reviewId);
            return currentUser != null && review != null && currentUser.getUserId() == review.getUserId();
        } catch (Exception e) {
            return false;
        }
    }
}
