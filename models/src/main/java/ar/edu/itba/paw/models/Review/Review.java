package ar.edu.itba.paw.models.Review;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.BadgeLimits;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reviews",uniqueConstraints = @UniqueConstraint(columnNames = {"userid", "mediaid"}))
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_reviewid_seq")
    @SequenceGenerator(sequenceName = "review_reviewid_seq", name = "review_reviewid_seq", allocationSize = 1)
    @Column(name = "reviewid")
    private int reviewId;

    /*
    @Column(name = "userid", nullable = false)
    private int userId;
    @Formula("(SELECT u.username FROM users u WHERE u.userid = userId)")
    private String username;

    @Formula("(SELECT " +
            "(SELECT COUNT(rl.reviewid) FROM reviewslikes rl LEFT OUTER JOIN reviews r ON r.reviewid = rl.reviewid WHERE r.userid = userId) + " +
            "(SELECT COUNT(mlrl.moovielistreviewid) FROM moovielistsreviewslikes mlrl LEFT OUTER JOIN moovielistsreviews mlr ON mlr.moovielistreviewid = mlrl.moovielistreviewid WHERE mlr.userid = userId) + " +
            "(SELECT COUNT(c.commentid) FROM commentlikes cl LEFT OUTER JOIN comments c ON c.commentid = cl.commentid WHERE c.userid = userId) + " +
            "(SELECT COUNT(ml.moovielistid) FROM moovielistslikes mll LEFT OUTER JOIN moovielists ml ON ml.moovielistid = mll.moovielistid WHERE ml.userid = userId) )")
    private int milkyPoints; */

    @Column(nullable = false)
    private int mediaId;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private int rating;

    @Column
    private LocalDate lastModified;

    @Formula("(SELECT COUNT(*) FROM reviewslikes WHERE reviewslikes.reviewid = reviewid)")
    private int reviewLikes;

    @Transient
    private boolean currentUserHasLiked = false;

    @Formula("(SELECT m.posterPath FROM media m WHERE m.mediaId = mediaId)")
    private String mediaPosterPath;

    @Formula("(SELECT m.name FROM media m WHERE m.mediaId = mediaId)")
    private String mediaTitle;

    @Column(length = 5000)
    private String reviewContent;

    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.reviewid = reviewId)")
    private Long commentCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewId", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewReport> reports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewsLikes> likes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Formula("(SELECT COUNT(*) FROM reportsreviews rr WHERE rr.reviewid = reviewId)")
    private int totalReports;
    @Formula("(SELECT COUNT(*) FROM reportsreviews rr WHERE rr.type = 3 AND rr.reviewid = reviewId)")
    private int spamReports;
    @Formula("(SELECT COUNT(*) FROM reportsreviews rr WHERE rr.type = 0 AND rr.reviewid = reviewId)")
    private int hateReports;
    @Formula("(SELECT COUNT(*) FROM reportsreviews rr WHERE rr.type = 2 AND rr.reviewid = reviewId)")
    private int privacyReports;
    @Formula("(SELECT COUNT(*) FROM reportsreviews rr WHERE rr.type = 1 AND rr.reviewid = reviewId)")
    private int abuseReports;

    //hibernate
    Review() {
    }

    public Review(User user, int mediaId, int rating, String reviewContent) {
        this.user = user;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.lastModified = LocalDate.now();
    }

    public Review(User user, int reviewId, int mediaId, int rating, int reviewLikes, LocalDate lastModified, String mediaPosterPath, String mediaTitle, String reviewContent, Long commentCount, List<Comment> comments) {
        this.user = user;
        this.reviewId = reviewId;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewLikes = reviewLikes;
        this.mediaPosterPath = mediaPosterPath;
        this.mediaTitle = mediaTitle;
        this.reviewContent = reviewContent;
        this.commentCount = commentCount;
        this.comments = comments;
        this.lastModified = lastModified;
    }


    public Review(Review r, int currentUserHasLiked){
        this.user = r.getUser();
        this.reviewId = r.reviewId;
        this.mediaId = r.mediaId;
        this.rating = r.rating;
        this.reviewLikes = r.reviewLikes;
        this.mediaPosterPath = r.mediaPosterPath;
        this.mediaTitle = r.mediaTitle;
        this.reviewContent = r.reviewContent;
        this.commentCount = r.commentCount;
        this.comments = r.comments;
        this.lastModified = r.lastModified;
        this.currentUserHasLiked = currentUserHasLiked==1;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public int getSpamReports() {
        return spamReports;
    }

    public int getHateReports() {
        return hateReports;
    }

    public int getPrivacyReports() {
        return privacyReports;
    }

    public int getAbuseReports() {
        return abuseReports;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getRating() {
        return rating;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public String getMediaPosterPath() {
        return mediaPosterPath;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setHasLiked(boolean b) {
        this.currentUserHasLiked = b;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isHasBadge() {
        return user.isHasBadge();
    }

    public User getUser() {
        return user;
    }
}
