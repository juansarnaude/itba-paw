package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> getComments(int reviewId, int size, int pageNumber);

    public Comment getCommentById(int commentId);

    public boolean userHasLiked(int commentId, int userId);
    public boolean userHasDisliked(int commentId, int userId);

    public boolean likeComment(int commentId);

    public void removeLikeComment(int commentId);

    public boolean dislikeComment(int commentId);

    public void removeDislikeComment(int commentId);

    public void createComment(int reviewId, String content);

    public void deleteComment(int commentId);
}
