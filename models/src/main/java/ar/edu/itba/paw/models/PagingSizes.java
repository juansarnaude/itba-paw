package ar.edu.itba.paw.models;

public enum PagingSizes {
    MEDIA_DEFAULT_PAGE_SIZE(25),
    MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS(24),
    MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT(25),
    FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT(100),
    REVIEW_DEFAULT_PAGE_SIZE(5),
    USER_LIST_DEFAULT_PAGE_SIZE(6),
    MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE(25);

    private final int size;

    PagingSizes(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
