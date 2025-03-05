package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateReviewForm {

    @Range(min = 1, message = "Please enter a valid media id")
    private int mediaId;
    @Range(min = 1, max = 5, message = "Please enter a rating between 1 and 5")
    private int rating;
    @Pattern(regexp = "^(?!\\s+$)(?!.*[\\n\\r]).*$", message = "Review content must not contain enters or only spaces")
    @Size(max = 500, message = "Review content must not exceed 500 characters")
    private String reviewContent;

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }


    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getRating() {
        return rating;
    }
}