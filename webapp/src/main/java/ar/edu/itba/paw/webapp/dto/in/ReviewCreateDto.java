package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewCreateDto {

    @NotNull
    @Min(1)
    @Max(5)
    private int rating;

    @Size(min=0,max=500)
    private String reviewContent;

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
