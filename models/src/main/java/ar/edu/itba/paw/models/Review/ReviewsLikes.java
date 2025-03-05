package ar.edu.itba.paw.models.Review;

import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "reviewslikes",uniqueConstraints = @UniqueConstraint(columnNames = {"reviewId", "userId"}))
public class ReviewsLikes implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "reviewId", referencedColumnName = "reviewId")
    private Review review ;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public ReviewsLikes() {
    }

    public ReviewsLikes(Review review, User user) {
        this.review = review;
        this.user = user;
    }

    public Review getReview() {
        return review;
    }

    public User getUser() {
        return user;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
