package ar.edu.itba.paw.models.Genre;

import ar.edu.itba.paw.models.Media.Media;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genreId")
    private Integer genreId;

    @Column(name = "genreName", length = 100, nullable = false)
    private String genreName;


    @ManyToMany
    @JoinTable(
            name = "mediagenres",
            joinColumns = @JoinColumn(name = "genreId"),
            inverseJoinColumns = @JoinColumn(name = "mediaId")
    )
    private List<Media> medias;

    /* Just for Hibernate*/
    Genre(){

    }

    public Genre( final String genre) {
        this.genreName = genre;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public Integer getId() {
        return genreId;
    }

    public String getGenre() {
        return genreName;
    }
}
