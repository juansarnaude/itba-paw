package ar.edu.itba.paw.models.Cast;

import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actorId")
    private int actorId;

    @Column(name = "actorName", nullable = false)
    private String actorName;

    @Column(name = "profilePath")
    private String profilePath;


    @ManyToMany
    @JoinTable(
            name = "mediaactors",
            joinColumns = @JoinColumn(name = "actorId"),
            inverseJoinColumns = @JoinColumn(name = "mediaId")
    )
    private List<Media> medias;

    @Transient
    private String characterName;

    public Actor(int actorId, String actorName, String profilePath, String characterName) {
        this.actorId = actorId;
        this.actorName = actorName;
        this.profilePath = profilePath;
        this.characterName = characterName;
    }

    public Actor(Actor actor, String characterName) {
        this.actorId = actor.actorId;
        this.actorName = actor.actorName;
        this.profilePath = actor.profilePath;
        this.characterName = characterName;
    }

    public Actor(){}

    public int getActorId() {
        return actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getCharacterName() {
        return characterName;
    }

    public List<Media> getMedias() {
        return medias;
    }
}

