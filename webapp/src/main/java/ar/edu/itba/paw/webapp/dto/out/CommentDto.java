package ar.edu.itba.paw.webapp.dto.out;


import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Reports.ReportTypesEnum;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class CommentDto {

    private int id;

    private int reviewId;

    private int mediaId;

    private String content;

    private int commentLikes;

    private int commentDislikes;

    private String username;

    private String totalReportsUrl;

    private String spamReportsUrl;

    private String hateReportsUrl;

    private String abuseReportsUrl;

    private String privacyReportsUrl;

    private String url;

    private String userUrl;

    private String reviewUrl;

    private String reportsUrl;

    public static CommentDto fromComment(final Comment comment, UriInfo uriInfo) {
        CommentDto commentDto = new CommentDto();
        commentDto.id = comment.getCommentId();
        commentDto.reviewId = comment.getReviewId();
        commentDto.mediaId = comment.getMediaId();
        commentDto.content = comment.getContent();
        commentDto.commentLikes = comment.getCommentLikes();
        commentDto.commentDislikes = comment.getCommentDislikes();
        commentDto.username = comment.getUsername();

        commentDto.totalReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "comment")
                .queryParam("resourceId", commentDto.getId())
                .build()
                .toString();

        commentDto.spamReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "comment")
                .queryParam("resourceId", commentDto.getId())
                .queryParam("type", ReportTypesEnum.spam.getType())
                .build()
                .toString();

        commentDto.hateReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "comment")
                .queryParam("resourceId", commentDto.getId())
                .queryParam("type", ReportTypesEnum.hatefulContent.getType())
                .build()
                .toString();

        commentDto.privacyReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "comment")
                .queryParam("resourceId", commentDto.getId())
                .queryParam("type", ReportTypesEnum.privacy.getType())
                .build()
                .toString();

        commentDto.abuseReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "comment")
                .queryParam("resourceId", commentDto.getId())
                .queryParam("type", ReportTypesEnum.abuse.getType())
                .build()
                .toString();


        commentDto.url = uriInfo.getBaseUriBuilder().path("/comments/{id}").build(comment.getCommentId()).toString();
        commentDto.reviewUrl = uriInfo.getBaseUriBuilder().path("/reviews/{id}").build(comment.getReviewId()).toString();
        commentDto.userUrl = uriInfo.getBaseUriBuilder().path("/users/{username}").build(comment.getUsername()).toString();

        return commentDto;

    }

    public static List<CommentDto> fromCommentList(final List<Comment> commentList, final UriInfo uriInfo) {
        return commentList.stream().map(r -> fromComment(r, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(int commentLikes) {
        this.commentLikes = commentLikes;
    }

    public int getCommentDislikes() {
        return commentDislikes;
    }

    public void setCommentDislikes(int commentDislikes) {
        this.commentDislikes = commentDislikes;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getReportsUrl() {
        return reportsUrl;
    }

    public void setReportsUrl(String reportsUrl) {
        this.reportsUrl = reportsUrl;
    }

}
