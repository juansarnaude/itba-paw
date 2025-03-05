package ar.edu.itba.paw.models.User;

public enum BadgeLimits {
    POINTS_FOR_SIMPLE_BADGE(5);

    private final int limit;

    BadgeLimits(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
