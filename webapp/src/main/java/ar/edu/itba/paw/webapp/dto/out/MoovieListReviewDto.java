package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.List;

public class MoovieListReviewDto {

    private int id;

    private int moovieListid;

    private int reviewLikes;

    private LocalDate lastModified;

    private String reviewContent;

    private String totalReportsUrl;

    private String spamReportsUrl;

    private String hateReportsUrl;

    private String abuseReportsUrl;

    private String privacyReportsUrl;
    private String username;

    private String url;

    private String userUrl;

    private String moovieListUrl;


    public static MoovieListReviewDto fromMoovieListReview(final MoovieListReview moovieListReview, UriInfo uriInfo){
        MoovieListReviewDto moovieListReviewDto= new MoovieListReviewDto();
        moovieListReviewDto.id=moovieListReview.getMoovieListReviewId();
        moovieListReviewDto.moovieListid=moovieListReview.getMoovieListId();
        moovieListReviewDto.reviewLikes=moovieListReview.getReviewLikes();
        moovieListReviewDto.reviewContent= moovieListReview.getReviewContent();
        moovieListReviewDto.username = moovieListReview.getUser().getUsername();
        moovieListReviewDto.lastModified = moovieListReview.getLastModified();

        moovieListReviewDto.totalReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .build()
                .toString();

        moovieListReviewDto.spamReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.spam.getType())
                .build()
                .toString();

        moovieListReviewDto.hateReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.hatefulContent.getType())
                .build()
                .toString();

        moovieListReviewDto.privacyReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.privacy.getType())
                .build()
                .toString();

        moovieListReviewDto.abuseReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.abuse.getType())
                .build()
                .toString();




        moovieListReviewDto.url= uriInfo.getBaseUriBuilder().path("/moovieListReview/{id}").build(moovieListReview.getMoovieListReviewId()).toString();
        moovieListReviewDto.userUrl=uriInfo.getBaseUriBuilder().path("/users/username/{username}").build(moovieListReview.getUser().getUsername()).toString();
        moovieListReviewDto.moovieListUrl=uriInfo.getBaseUriBuilder().path("/lists/{id}").build(moovieListReview.getMoovieListId()).toString();


        return moovieListReviewDto;
    }

    public static List<MoovieListReviewDto> fromMoovieListReviewList(final List<MoovieListReview> moovieListReviews,UriInfo uriInfo){
        return moovieListReviews.stream().map(r->fromMoovieListReview(r,uriInfo)).collect(java.util.stream.Collectors.toList());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoovieListid() {
        return moovieListid;
    }

    public void setMoovieListid(int moovieListid) {
        this.moovieListid = moovieListid;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public void setReviewLikes(int reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalReportsUrl() {
        return totalReportsUrl;
    }

    public void setTotalReportsUrl(String totalReportsUrl) {
        this.totalReportsUrl = totalReportsUrl;
    }

    public String getSpamReportsUrl() {
        return spamReportsUrl;
    }

    public void setSpamReportsUrl(String spamReportsUrl) {
        this.spamReportsUrl = spamReportsUrl;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getHateReportsUrl() {
        return hateReportsUrl;
    }

    public void setHateReportsUrl(String hateReportsUrl) {
        this.hateReportsUrl = hateReportsUrl;
    }

    public String getAbuseReportsUrl() {
        return abuseReportsUrl;
    }

    public String getPrivacyReportsUrl() {
        return privacyReportsUrl;
    }

    public void setPrivacyReportsUrl(String privacyReportsUrl) {
        this.privacyReportsUrl = privacyReportsUrl;
    }

    public void setAbuseReportsUrl(String abuseReportsUrl) {
        this.abuseReportsUrl = abuseReportsUrl;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getMoovieListUrl() {
        return moovieListUrl;
    }

    public void setMoovieListUrl(String moovieListUrl) {
        this.moovieListUrl = moovieListUrl;
    }
}
