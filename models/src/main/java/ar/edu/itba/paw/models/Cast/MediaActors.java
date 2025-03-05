package ar.edu.itba.paw.models.Cast;

import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "mediaactors", uniqueConstraints = @UniqueConstraint(columnNames = {"mediaId", "actorId"}))
public class MediaActors implements Serializable {

    @Id
    @ManyToOne
    @MapsId("mediaId")
    @JoinColumn(name = "mediaId", referencedColumnName = "mediaId")
    private Media media;

    @Id
    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actorId", referencedColumnName = "actorId")
    private Actor actor;

    @Column(name = "charactername")
    private String characterName;
}

