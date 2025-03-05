package ar.edu.itba.paw.models.Media;

public enum MediaTypes {
    TYPE_MOVIE(0),
    TYPE_TVSERIE(1),
    TYPE_ALL(2);

    private final int type;
    MediaTypes(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
