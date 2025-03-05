package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.JOINED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_mediaid_seq")
    @SequenceGenerator(sequenceName = "media_mediaid_seq", name = "media_mediaid_seq", allocationSize = 1)
    @Column(name = "mediaId")
    private int mediaId;

    @Column(nullable = false)
    private boolean type;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 2)
    private String originalLanguage;

    @Column(nullable = false)
    private boolean adult;

    @Temporal(TemporalType.DATE)
    @Column
    private Date releaseDate;

    @Column(nullable = false, length = 5000)
    private String overview;

    @Column(length = 255)
    private String backdropPath;

    @Column(length = 255)
    private String posterPath;

    @Column(length = 255)
    private String trailerLink;

    @Column(nullable = false)
    private float tmdbRating;

    @Column(length = 20, nullable = false)
    private String status;

    @Formula("(SELECT COALESCE(AVG(r.rating), 0) FROM reviews r WHERE mediaid = r.mediaid)")
    private float totalRating;

    @Formula("(SELECT COUNT(r.rating) FROM reviews r WHERE mediaid = r.mediaid)")
    private int voteCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mediaproviders",
            joinColumns = @JoinColumn(name = "mediaId"),
            inverseJoinColumns = @JoinColumn(name = "providerId"))
    private List<Provider> providers = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mediagenres",
            joinColumns = @JoinColumn(name = "mediaId"),
            inverseJoinColumns = @JoinColumn(name = "genreId"))
    private List<Genre> genres = new ArrayList<>();

    @Transient
    private boolean watched;

    @Transient
    private boolean watchlist;



    /* Just for Hibernate*/
    public Media(){

    }

    public Media(final int mediaId, final boolean type, final String name, final String originalLanguage, final boolean adult, final Date releaseDate, final String overview,
                 final String backdropPath, final String posterPath, final String trailerLink, final float tmdbRating, final float totalRating, final int voteCount, final String status,
                 final List<Genre> genres, final List<Provider> providers) {
        this.mediaId = mediaId;
        this.type = type;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
        this.genres = genres;
        this.providers = providers;
    }

    public Media(final int mediaId, final boolean type, final String name, final String originalLanguage, final boolean adult, final Date releaseDate, final String overview,
                 final String backdropPath, final String posterPath, final String trailerLink, final float tmdbRating, final float totalRating, final int voteCount, final String status,
                 final List<Genre> genres, final List<Provider> providers, boolean watched, boolean watchlist) {
        this.mediaId = mediaId;
        this.type = type;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
        this.genres = genres;
        this.providers = providers;
        this.watched = watched;
        this.watchlist = watchlist;
    }

    public Media(Media media, boolean watched, boolean watchlist){
        this.mediaId = media.mediaId;
        this.type = media.type;
        this.name = media.name;
        this.originalLanguage = media.originalLanguage;
        this.adult = media.adult;
        this.releaseDate = media.releaseDate;
        this.overview = media.overview;
        this.backdropPath = media.backdropPath;
        this.posterPath = media.posterPath;
        this.trailerLink = media.trailerLink;
        this.tmdbRating = media.tmdbRating;
        this.totalRating = media.totalRating;
        this.voteCount = media.voteCount;
        this.status = media.status;
        this.genres = media.genres;
        this.providers = media.providers;
        this.watched = watched;
        this.watchlist = watchlist;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Media that = (Media) obj;
        return mediaId == that.mediaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaId);
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public void setTmdbRating(float tmdbRating) {
        this.tmdbRating = tmdbRating;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getMediaId() {
        return mediaId;
    }

    public boolean isType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public boolean isAdult() {
        return adult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public float getTmdbRating() {
        return tmdbRating/2;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : this.genres) {
            genres.add(genre);
        }
        return genres;
    }

    public List<Genre> getGenresModels(){
        return this.genres;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public boolean isWatched() {
        return watched;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }
}
