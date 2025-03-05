package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;

public class UserReviewIdDto {
    private int reviewId;
    private String username;

    public UserReviewIdDto() {}

    public static UserReviewIdDto fromUserReviewId(int reviewId, String username) {
        UserReviewIdDto dto = new UserReviewIdDto();
        dto.reviewId = reviewId;
        dto.username = username;
        return dto;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
