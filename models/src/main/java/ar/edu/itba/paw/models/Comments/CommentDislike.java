package ar.edu.itba.paw.models.Comments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "commentdislikes")
public class CommentDislike implements Serializable {

    @Id
    @Column(name = "commentid")
    private int commentId;

    @Id
    @Column(name = "userid")
    private int userId;

    CommentDislike(){}

    public CommentDislike(int commentId, int userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
