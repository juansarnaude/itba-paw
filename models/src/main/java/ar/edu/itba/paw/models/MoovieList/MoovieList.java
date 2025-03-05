package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="moovielists",uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "name"}) )
public class MoovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielists_moovielistid_seq")
    @SequenceGenerator(sequenceName = "moovielists_moovielistid_seq", name = "moovielists_moovielistid_seq", allocationSize = 1)
    @Column(name = "moovielistId")
    private int moovieListId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, length = 5000)
    private String description;

    @Column(nullable = false)
    private int type;

    @Formula("(SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.moovieListId = moovieListId)")
    private int totalReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 3 AND rr.moovieListId = moovieListId)")
    private int spamReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 0 AND rr.moovieListId = moovieListId)")
    private int hateReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 2 AND rr.moovieListId = moovieListId)")
    private int privacyReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 1 AND rr.moovieListId = moovieListId)")
    private int abuseReports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieList", cascade = CascadeType.ALL)
    private List<MoovieListLikes> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieList", cascade = CascadeType.ALL)
    private List<MoovieListFollowers>  followers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieList", cascade = CascadeType.ALL)
    private List<MoovieListContent> content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieListReviewId", cascade = CascadeType.ALL)
    private List<MoovieListReview> reviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieList", cascade = CascadeType.ALL)
    private  List<MoovieListReport> reports;

    public MoovieList(){}

    public MoovieList(int userId, String name, String description, int type) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

//    public Set<MoovieListFollowers> getFollowers() {
//        return followers;
//    }
//
//    public Set<MoovieListLikes> getLikes() {
//        return likes;
//    }


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

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoovieList that = (MoovieList) o;
        return moovieListId == that.moovieListId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moovieListId);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

