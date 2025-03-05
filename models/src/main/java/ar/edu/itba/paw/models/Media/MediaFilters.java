package ar.edu.itba.paw.models.Media;

public enum MediaFilters {
    NAME("name"),
    TOTALRATING("totalRating"),
    TMDBRATING("tmdbRating"),
    RELEASE_DATE("releaseDate"),
    VOTECOUNT("voteCount"),
    CUSTOM_ORDER("customOrder"),
    ASC("ASC"),
    DESC("DESC");

    private final String filter;

    MediaFilters(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}
