package ar.edu.itba.paw.models.Comments;

public enum CommentFeedbackType {
    LIKE("feedback.like"),DISLIKE("feedback.dislike");

    private final String code;

    CommentFeedbackType(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    @Override
    public String toString(){
        return code;
    }
}

