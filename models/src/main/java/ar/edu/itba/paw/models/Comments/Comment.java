package ar.edu.itba.paw.models.Comments;


import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(name = "reviewId", nullable = false)
    private int reviewId;

    @Formula("(SELECT m.mediaid FROM media m JOIN reviews r ON r.mediaid = m.mediaid WHERE r.reviewid = reviewId )")
    private int mediaId;

    @Column(name = "content", length = 5000)
    private String content;

    @Transient
    private boolean currentUserHasLiked = false;

    @Transient
    private boolean currentUserHasDisliked = false;

    @Formula("(SELECT COUNT(*) FROM commentlikes cl WHERE cl.commentId = commentId)")
    private int commentLikes;

    @Formula("(SELECT COUNT(*) FROM commentdislikes cl WHERE cl.commentId = commentId)")
    private int commentDislikes;


    @Formula("CASE WHEN EXISTS (SELECT 1 FROM userimages ui WHERE ui.userid = userId) THEN 1 ELSE 0 END")
    private boolean hasPfp;


    @Formula("(SELECT COUNT(*) FROM reportscomments rc WHERE rc.commentid = commentId)")
    private int totalReports;
    @Formula("(SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 3 AND rc.commentid = commentId)")
    private int spamReports;
    @Formula("(SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 0 AND rc.commentid = commentId)")
    private int hateReports;
    @Formula("(SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 2 AND rc.commentid = commentId)")
    private int privacyReports;
    @Formula("(SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 1 AND rc.commentid = commentId)")
    private int abuseReports;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    List<CommentReport> commentReports;

    Comment() {

    }

    public Comment(final User user, final int reviewId, final String content) {
        this.reviewId = reviewId;
        this.user = user;
        this.content = content;
    }

    public Comment(Comment comment, boolean currentUserHasLiked, boolean currentUserHasDisliked){
        this.reviewId = comment.reviewId;
        this.user = comment.user;
        this.content = comment.content;

        this.currentUserHasLiked = currentUserHasLiked;
        this.currentUserHasDisliked = currentUserHasDisliked;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCommentLikes(int commentLikes) {
        this.commentLikes = commentLikes;
    }

    public void setCommentDislikes(int commentDislikes) {
        this.commentDislikes = commentDislikes;
    }

    public boolean isHasPfp() {
        return hasPfp;
    }

    public void setHasPfp(boolean hasPfp) {
        this.hasPfp = hasPfp;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }

    public int getSpamReports() {
        return spamReports;
    }

    public void setSpamReports(int spamReports) {
        this.spamReports = spamReports;
    }

    public int getHateReports() {
        return hateReports;
    }

    public void setHateReports(int hateReports) {
        this.hateReports = hateReports;
    }

    public int getPrivacyReports() {
        return privacyReports;
    }

    public void setPrivacyReports(int privacyReports) {
        this.privacyReports = privacyReports;
    }

    public int getAbuseReports() {
        return abuseReports;
    }

    public void setAbuseReports(int abuseReports) {
        this.abuseReports = abuseReports;
    }

    public boolean isCurrentUserHasDisliked() {
        return currentUserHasDisliked;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public void setCurrentUserHasDisliked(boolean currentUserHasDisliked) {
        this.currentUserHasDisliked = currentUserHasDisliked;
    }

    public boolean getUserPfp() {
        return hasPfp;
    }

    public int getCommentDislikes() {
        return commentDislikes;
    }

    public int getCommentLikes() {
        return commentLikes;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public int getCommentId() {
        return commentId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public boolean isHasBadge() {
        return user.isHasBadge();
    }

}
