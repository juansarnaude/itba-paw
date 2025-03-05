package ar.edu.itba.paw.models.Review;

public enum ReviewTypes {
    REVIEW_MEDIA(1),
    REVIEW_MOOVIE_LIST(2);

    private final int type;
    ReviewTypes(int size) {
        this.type = size;
    }

    public int getType() {
        return type;
    }
}

