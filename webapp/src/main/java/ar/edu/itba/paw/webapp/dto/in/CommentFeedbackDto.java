package ar.edu.itba.paw.webapp.dto.in;

import ar.edu.itba.paw.models.Comments.CommentFeedbackType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CommentFeedbackDto {

    @Pattern(regexp ="^(LIKE|DISLIKE)$")
    @NotNull
    private String feedbackType;

    public CommentFeedbackDto() {
    }

    public CommentFeedbackDto(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public CommentFeedbackType transformToEnum() {
        return CommentFeedbackType.valueOf(feedbackType);
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

}
