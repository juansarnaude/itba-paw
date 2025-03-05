package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MoovieListReviewCreateDto {

    @NotNull
    @Size(min=1,max=500)
    private String reviewContent;

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
