package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "movies")
@PrimaryKeyJoinColumn(name = "mediaId")
public class Movie extends Media{
    @Column
    private Integer runtime;

    @Column
    private Long budget;

    @Column
    private Long revenue;

    @Column
    private Integer directorId;

    @Column
    private String director;

    public Movie(){ }

    public Movie(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview, String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount, String status, List<Genre> genres, List<Provider> providers, int runtime, long budget, long revenue, int directorId, String director) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
        this.directorId = directorId;
        this.director = director;
    }

    public Movie(Movie movie, boolean watched, boolean watchlist){
        super(movie.getMediaId(), movie.isType(), movie.getName(), movie.getOriginalLanguage(), movie.isAdult(), movie.getReleaseDate(), movie.getOverview(), movie.getBackdropPath(), movie.getPosterPath(), movie.getTrailerLink(), movie.getTmdbRating(), movie.getTotalRating(), movie.getVoteCount(), movie.getStatus(), movie.getGenresModels(), movie.getProviders());
        this.setWatched(watched);
        this.setWatchlist(watchlist);
        this.runtime = movie.runtime;
        this.budget = movie.budget;
        this.revenue = movie.revenue;
        this.directorId = movie.directorId;
        this.director = movie.director;
    }

    public int getRuntime() {
        return runtime;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getDirector() {
        return director;
    }
}
