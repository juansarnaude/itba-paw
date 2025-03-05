package ar.edu.itba.paw.models.Review;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "moovieListsReviews", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "moovieListId"}))
public class MoovieListReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielistsreviews_moovielistreviewid_seq")
    @SequenceGenerator(sequenceName = "moovielistsreviews_moovielistreviewid_seq", name = "moovielistsreviews_moovielistreviewid_seq", allocationSize = 1)
    @Column(name = "moovieListReviewId")
    private int moovieListReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(nullable = false)
    private int moovieListId;

    @Column
    private LocalDate lastModified;

    @Formula("(SELECT COUNT(*) FROM moovieListsReviewsLikes WHERE moovieListsReviewsLikes.moovieListReviewId = moovieListReviewId)")
    private int reviewLikes;
    @Transient
    private boolean currentUserHasLiked = false;
    @Formula("( SELECT ARRAY_AGG(m.posterPath) FROM moovielistscontent mlc INNER JOIN media m ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = moovieListId )")
    private String moovieListImages;

    @Formula("(SELECT m.name FROM moovieLists m WHERE m.moovielistid = moovielistid)")
    private String moovieListTitle;

    @Column(length = 5000)
    private String reviewContent;

    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.moovieListReviewId = moovieListReviewId)")
    private int totalReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 3 AND rr.moovieListReviewId = moovieListReviewId)")
    private int spamReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 0 AND rr.moovieListReviewId = moovieListReviewId)")
    private int hateReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 2 AND rr.moovieListReviewId = moovieListReviewId)")
    private int privacyReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 1 AND rr.moovieListReviewId = moovieListReviewId)")
    private int abuseReports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieListReview", cascade = CascadeType.ALL)
    private List<MoovieListsReviewsLikes> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moovieListReview", cascade = CascadeType.ALL)
    private List<MoovieListReviewReport> reports;

    //hibernate
    public MoovieListReview() {
    }

    public MoovieListReview(User user, int moovieListId, String reviewContent) {
        this.user = user;
        this.moovieListId = moovieListId;
        this.reviewContent = reviewContent;
        this.lastModified = LocalDate.now();
    }

    public MoovieListReview(User user, int moovieListReviewId,  int moovieListId, int reviewLikes, boolean currentUserHasLiked, String moovieListImages, String moovieListTitle, String reviewContent) {
        this.moovieListReviewId = moovieListReviewId;
        this.user = user;
        this.moovieListId = moovieListId;
        this.reviewLikes = reviewLikes;
        this.currentUserHasLiked = currentUserHasLiked;
        this.moovieListImages = moovieListImages;
        this.moovieListTitle = moovieListTitle;
        this.reviewContent = reviewContent;
        this.lastModified = LocalDate.now();
    }

    public MoovieListReview(User user, int moovieListReviewId, LocalDate lastModified, int moovieListId, int reviewLikes, String moovieListImages, String moovieListTitle, String reviewContent) {
        this.moovieListReviewId = moovieListReviewId;
        this.user = user;
        this.moovieListId = moovieListId;
        this.reviewLikes = reviewLikes;
        this.moovieListImages = moovieListImages;
        this.moovieListTitle = moovieListTitle;
        this.reviewContent = reviewContent;
        this.lastModified = lastModified;
    }

    public MoovieListReview(MoovieListReview m, int currentUserHasLiked){
        this.user = m.user;
        this.moovieListReviewId = m.moovieListReviewId;
        this.moovieListId = m.moovieListId;
        this.reviewLikes = m.reviewLikes;
        this.currentUserHasLiked = currentUserHasLiked == 1;
        this.moovieListImages = m.moovieListImages;
        this.moovieListTitle = m.moovieListTitle;
        this.reviewContent = m.reviewContent;
        this.lastModified = m.lastModified;
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

    public void setMoovieListReviewId(int moovieListReviewId) {
        this.moovieListReviewId = moovieListReviewId;
    }


    public void setMoovieListId(int mediaId) {
        this.moovieListId = mediaId;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public void setReviewLikes(int reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public void setMoovieListImages(String moovieListImages) {
        this.moovieListImages = moovieListImages;
    }

    public void setMoovieListTitle(String moovieListTitle) {
        this.moovieListTitle = moovieListTitle;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getMoovieListReviewId() {
        return moovieListReviewId;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMoovieListImages(){
        return moovieListImages;
    }

    /*
    COMMENTED BECAUSE IT FAILED TRANSACTIONS
    public List<String> getMoovieListImages() {
        List<String> toRet = new ArrayList<>();
        if(this.moovieListImages!=null){
            String[] aux = this.moovieListImages.replaceAll("[{}]","").split(",");
            toRet = new ArrayList<>(Arrays.asList(aux));
            if (toRet.size() > 4) {
                toRet = toRet.subList(0, 4);
            }
        }else{
            toRet = new ArrayList<>();
        }
        return toRet;
    }
    */


    public String getMoovieListTitle() {
        return moovieListTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public boolean isHasBadge() {
        return user.isHasBadge();
    }

}
