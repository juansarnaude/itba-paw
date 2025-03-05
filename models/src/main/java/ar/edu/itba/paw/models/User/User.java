package ar.edu.itba.paw.models.User;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}), @UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
    @SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
    private Integer userId;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private int role;

    @Formula("(SELECT CASE WHEN EXISTS (SELECT 1 FROM userimages ui WHERE ui.userid = userId) THEN 1 ELSE 0 END) ")
    private boolean hasPfp;

    @Formula("(SELECT " +
            "(SELECT COUNT(rl.reviewid) FROM reviewslikes rl LEFT OUTER JOIN reviews r ON r.reviewid = rl.reviewid WHERE r.userid = userId) + " +
            "(SELECT COUNT(mlrl.moovielistreviewid) FROM moovielistsreviewslikes mlrl LEFT OUTER JOIN moovielistsreviews mlr ON mlr.moovielistreviewid = mlrl.moovielistreviewid WHERE mlr.userid = userId) + " +
            "(SELECT COUNT(c.commentid) FROM commentlikes cl LEFT OUTER JOIN comments c ON c.commentid = cl.commentid WHERE c.userid = userId) + " +
            "(SELECT COUNT(ml.moovielistid) FROM moovielistslikes mll LEFT OUTER JOIN moovielists ml ON ml.moovielistid = mll.moovielistid WHERE ml.userid = userId) )")
    private int milkyPoints;

    @Transient
    private boolean hasBadge;

//    @OneToMany(mappedBy = "user")
//    final private Set<MoovieListLikes> likes = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    final private Set<MoovieListFollowers> followers = new HashSet<>();

    //Para hibernate
    public User(){

    }

    public User(int userId, String username, String email, String password, int role, int milkyPoints) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.milkyPoints = milkyPoints;
    }

    public User(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.role =  builder.role;
        this.milkyPoints = builder.milkyPoints;
    }

//    public Set<MoovieListFollowers> getFollowers() {
//        return followers;
//    }
//
//    public Set<MoovieListLikes> getLikes() {
//        return likes;
//    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role){
        this.role = role;
    }

    public boolean getHasPfp(){ return hasPfp; }

    public int getMilkyPoints() {
        return milkyPoints;
    }

    public static class Builder {
        private final String email;
        private final String username;
        private final String password;
        private final int role;
        private final int milkyPoints;
        private Integer userId = null;

        public Builder(String username, String email, String password, int role, int milkyPoints) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.role = role;
            this.milkyPoints = milkyPoints;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public boolean isHasBadge() {
        return milkyPoints >= BadgeLimits.POINTS_FOR_SIMPLE_BADGE.getLimit();
    }


}
