package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MoovieListFeedbackDto {

    @Pattern(regexp ="^(LIKE|UNLIKE)$")
    @NotNull
    private String feedbackType;

    @NotNull
    private String username;

    public MoovieListFeedbackDto() {
    }

    public MoovieListFeedbackDto(String feedbackType) {
        this.feedbackType = feedbackType;
    }


    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
