package ar.edu.itba.paw.models.Provider;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "mediaproviders", uniqueConstraints = @UniqueConstraint(columnNames = {"mediaId", "providerId"}))
public class MediaProviders implements Serializable {
    @Id
    @ManyToOne
    @MapsId("mediaId")
    @JoinColumn(name = "mediaId", referencedColumnName = "mediaId")
    private Media media;

    @Id
    @ManyToOne
    @MapsId("providerId")
    @JoinColumn(name = "providerId", referencedColumnName = "providerId")
    private Provider provider;
}
