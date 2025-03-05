package ar.edu.itba.paw.models.User;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Profile {
    @Id
    private int userId;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private int role;
    @Formula("(SELECT COUNT(*) FROM moovieLists ml WHERE ml.userId = userId AND ml.type = 1)")
    private int moovieListCount;

    //ReviewsCount is the sum of reviews + moovielistreviews + comments
    @Formula("(SELECT COUNT(*) FROM reviews r WHERE r.userId = userId)")
    private int reviewsCount;


    @Formula("(SELECT " +
            "(SELECT COUNT(rl.reviewid) FROM reviewslikes rl LEFT OUTER JOIN reviews r ON r.reviewid = rl.reviewid WHERE r.userid = userId) + " +
            "(SELECT COUNT(mlrl.moovielistreviewid) FROM moovielistsreviewslikes mlrl LEFT OUTER JOIN moovielistsreviews mlr ON mlr.moovielistreviewid = mlrl.moovielistreviewid WHERE mlr.userid = userId) + " +
            "(SELECT COUNT(c.commentid) FROM commentlikes cl LEFT OUTER JOIN comments c ON c.commentid = cl.commentid WHERE c.userid = userId) + " +
            "(SELECT COUNT(ml.moovielistid) FROM moovielistslikes mll LEFT OUTER JOIN moovielists ml ON ml.moovielistid = mll.moovielistid WHERE ml.userid = userId) )")
    private int milkyPoints;

    @Formula("(SELECT EXISTS( SELECT 1 FROM userimages ui WHERE userid = ui.userid))")
    private boolean hasPfp;

    @Transient
    private boolean hasBadge;

    public Profile(){

    }

    public Profile(int userId, String username, String email, int role, int moovieListCount, int reviewsCount, int milkyPoints, boolean hasPfp) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.moovieListCount = moovieListCount;
        this.reviewsCount = reviewsCount;
        this.milkyPoints = milkyPoints;
        this.hasPfp = hasPfp;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getRole() {
        return role;
    }

    public int getMoovieListCount() {
        return moovieListCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }


    public int getMilkyPoints() {
        return milkyPoints;
    }

    public boolean isHasBadge() {
        return milkyPoints >= BadgeLimits.POINTS_FOR_SIMPLE_BADGE.getLimit();
    }

    public boolean isHasPfp() {
        return hasPfp;
    }
}
