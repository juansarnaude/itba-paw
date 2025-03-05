package ar.edu.itba.paw.models.Provider;

import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "providers")
public class Provider {
    @Id
    @Column(name = "providerId")
    private int providerId;

    @Column(name = "providerName", length = 100, nullable = false)
    private String providerName;

    @Column(name = "logoPath", length = 100, nullable = false)
    private String logoPath;

    @ManyToMany
    @JoinTable(
            name = "mediaproviders",
            joinColumns = @JoinColumn(name = "providerId"),
            inverseJoinColumns = @JoinColumn(name = "mediaId")
    )
    private List<Media> medias;

    /* Just for Hibernate*/
    Provider(){

    }

    public Provider(final int providerId, final String providerName, final String logoPath) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.logoPath = logoPath;

    }


    public int getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getLogoPath() {
        return logoPath;
    }
}
