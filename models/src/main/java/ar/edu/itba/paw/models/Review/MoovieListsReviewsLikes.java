package ar.edu.itba.paw.models.Review;

import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "moovielistsreviewslikes",uniqueConstraints = @UniqueConstraint(columnNames = {"moovieListReviewId", "userId"}))
public class MoovieListsReviewsLikes implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "moovieListReviewId", referencedColumnName = "moovieListReviewId")
    private MoovieListReview moovieListReview ;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public MoovieListsReviewsLikes() {
    }

    public MoovieListsReviewsLikes(MoovieListReview moovieList, User user) {
        this.moovieListReview = moovieList;
        this.user = user;
    }

    // Getters and setters
    public MoovieListReview getMoovieListReview() {
        return moovieListReview;
    }

    public void setMoovieListReview(MoovieListReview moovieListReviewId) {
        this.moovieListReview = moovieListReviewId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}