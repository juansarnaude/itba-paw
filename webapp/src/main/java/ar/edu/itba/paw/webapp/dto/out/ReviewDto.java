package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.Review;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.List;

public class ReviewDto {

    private int id;

    private int mediaId;

    private int rating;

    private String reviewContent;

    private int likes;

    private String username;

    private LocalDate lastModified;

    private String totalReportsUrl;

    private String spamReportsUrl;

    private String hateReportsUrl;

    private String privacyReportsUrl;

    private String abuseReportsUrl;

    private String url;

    private String userUrl;

    private String mediaUrl;


    public static ReviewDto fromReview(final Review review, UriInfo uriInfo) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.id = review.getReviewId();
        reviewDto.mediaId = review.getMediaId();
        reviewDto.rating = review.getRating();
        reviewDto.reviewContent = review.getReviewContent();
        reviewDto.likes = review.getReviewLikes();
        reviewDto.username = review.getUser().getUsername();
        reviewDto.lastModified = review.getLastModified();



        reviewDto.url = uriInfo.getBaseUriBuilder().path("/review/{id}").build(review.getReviewId()).toString();
        reviewDto.userUrl = uriInfo.getBaseUriBuilder().path("/users/{username}").build(review.getUser().getUsername()).toString();
        reviewDto.mediaUrl = uriInfo.getBaseUriBuilder().path("/medias/{id}").build(review.getMediaId()).toString();

        reviewDto.totalReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "review")
                .queryParam("resourceId", review.getReviewId())
                .build()
                .toString();

        reviewDto.spamReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "review")
                .queryParam("resourceId", review.getReviewId())
                .queryParam("type", ReportTypesEnum.spam.getType())
                .build()
                .toString();

        reviewDto.hateReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "review")
                .queryParam("resourceId", review.getReviewId())
                .queryParam("type", ReportTypesEnum.hatefulContent.getType())
                .build()
                .toString();

        reviewDto.privacyReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "review")
                .queryParam("resourceId", review.getReviewId())
                .queryParam("type", ReportTypesEnum.privacy.getType())
                .build()
                .toString();
        reviewDto.abuseReportsUrl = uriInfo.getBaseUriBuilder()
                .path("/reports/count")
                .queryParam("contentType", "review")
                .queryParam("resourceId", review.getReviewId())
                .queryParam("type", ReportTypesEnum.abuse.getType())
                .build()
                .toString();


        return reviewDto;
    }

    public static List<ReviewDto> fromReviewList(final List<Review> reviews, UriInfo uriInfo) {
        return reviews.stream().map(r -> fromReview(r, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }
}
