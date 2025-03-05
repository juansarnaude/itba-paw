package ar.edu.itba.paw.webapp.dto.out;

public class UserCommentIdDto {
    private int commentId;
    private String username;
    private boolean liked;
    private boolean disliked;

    public UserCommentIdDto() {}

    public static UserCommentIdDto fromUserCommentId(int commentId, String username, boolean liked, boolean disliked) {
        UserCommentIdDto dto = new UserCommentIdDto();
        dto.commentId = commentId;
        dto.username = username;
        dto.liked = liked;
        dto.disliked = disliked;
        return dto;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }
}
