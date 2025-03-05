package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Media.MediaTypes;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum FeaturedMoovieListEnum {
    topRatedMovies("Top Rated Movies",MediaTypes.TYPE_MOVIE.getType(),"tmdbRating"),
    topRatedSeries("Top Rated TV Series",MediaTypes.TYPE_TVSERIE.getType(),"tmdbRating"),
    topRatedMedia("Top Rated Media",MediaTypes.TYPE_ALL.getType(),"tmdbRating"),
    mostPopularMovies("Most Popular Movies",MediaTypes.TYPE_MOVIE.getType(),"voteCount"),
    mostPopularSeries("Most Popular TV Series",MediaTypes.TYPE_TVSERIE.getType(),"voteCount"),
    mostPopularMedia("Most Popular Media",MediaTypes.TYPE_ALL.getType(),"voteCount");

    private final String name;
    private final int type;
    private final String order;

    FeaturedMoovieListEnum(String name, int type, String order) {
        this.name = name;
        this.type = type;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getOrder() {
        return order;
    }

    private static final Map<String, FeaturedMoovieListEnum> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));

    public static Optional<FeaturedMoovieListEnum> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }
}
