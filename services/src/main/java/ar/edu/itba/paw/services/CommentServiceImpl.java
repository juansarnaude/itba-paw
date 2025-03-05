package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private UserService userService;

    @Autowired
    private CommentDao commentDao;


    @Transactional(readOnly = true)
    @Override
    public List<Comment> getComments(int reviewId, int size, int pageNumber) {
        return commentDao.getComments(reviewId, userService.tryToGetCurrentUserId(), size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(int commentId) {
        return commentDao.getCommentById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userHasLiked(int commentId, int userId) {
        return commentDao.userHasLiked(commentId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userHasDisliked(int commentId, int userId) {
        return commentDao.userHasDisliked(commentId,userId);
    }

    @Transactional
    @Override
    public boolean likeComment(int commentId) {
        User currentUser = userService.getInfoOfMyUser();
        boolean hasLiked = commentDao.userHasLiked(commentId, currentUser.getUserId());
        boolean hasDisliked = commentDao.userHasDisliked(commentId, currentUser.getUserId());
        if (!hasDisliked){
            if (!hasLiked){
                commentDao.likeComment(commentId, currentUser.getUserId());
                return true;
            }else{
                commentDao.removeLikeComment(commentId,currentUser.getUserId());
                return false;
            }
        }else{
            removeDislikeComment(commentId);
            likeComment(commentId);
            return true;
        }

    }

    @Transactional
    @Override
    public void removeLikeComment(int commentId) {
        commentDao.removeLikeComment(commentId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public boolean dislikeComment(int commentId) {
        User currentUser = userService.getInfoOfMyUser();
        boolean hasLiked = commentDao.userHasLiked(commentId, currentUser.getUserId());
        boolean hasDisliked = commentDao.userHasDisliked(commentId, currentUser.getUserId());
        if (!hasLiked){
            if (!hasDisliked){
                commentDao.dislikeComment(commentId, currentUser.getUserId());
                return true;
            }else{
                commentDao.removeDislikeComment(commentId,currentUser.getUserId());
                return false;
            }
        }else{
            removeLikeComment(commentId);
            dislikeComment(commentId);
            return true;
        }
    }

    @Transactional
    @Override
    public void removeDislikeComment(int commentId) {
        commentDao.removeDislikeComment(commentId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void createComment(int reviewId, String content) {
        commentDao.createComment(reviewId, content, userService.getInfoOfMyUser());
    }

    @Transactional
    @Override
    public void deleteComment(int commentId) {
        User currentUser = userService.getInfoOfMyUser();
        if (currentUser.getUserId() != commentDao.getCommentById(commentId).getUserId() && currentUser.getRole() != UserRoles.MODERATOR.getRole()){
            throw new AccessDeniedException("User is not the author of the comment");
        }

        commentDao.deleteComment(commentId);
    }
}
