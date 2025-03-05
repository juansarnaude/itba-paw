package ar.edu.itba.paw.models.TV;

import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "creators")
public class TVCreators {
    @Id
    private int creatorId;

    @Column(length = 100, nullable = false)
    private String creatorName;


    @ManyToMany
    @JoinTable(
            name = "mediacreators",
            joinColumns = @JoinColumn(name = "creatorId"),
            inverseJoinColumns = @JoinColumn(name = "mediaId")
    )
    private List<Media> medias;

    /* Just for Hibernate*/
    TVCreators(){

    }

    public TVCreators(int creatorId, String creatorName) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public List<Media> getMedias() {
        return medias;
    }
}
