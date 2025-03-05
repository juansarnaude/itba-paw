package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CommentForm {

    private int reviewId;
    private int listMediaId;
    @Pattern(regexp = "^(?!\\s+$)(?!.*[\\n\\r]).*$", message = "Comment or Review content must not contain enters or only spaces")
    @Size(max = 500, message = "Comment or Review content must not exceed 500 characters")
    @NotEmpty
    private String content;


    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setListMediaId(int listMediaId) {
        this.listMediaId = listMediaId;
    }

    public String getContent() {
        return content;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getListMediaId() {
        return listMediaId;
    }
}
