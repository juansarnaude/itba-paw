package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "moovielistscontent")
public class MoovieListContent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielistscontent_id_seq1")
    @SequenceGenerator(name = "moovielistscontent_id_seq1", sequenceName = "moovielistscontent_id_seq1", allocationSize = 1)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "moovieListId", referencedColumnName = "moovieListId")
    private MoovieList moovieList;

    @Column
    private int mediaId;

    @Column
    private int customOrder;

    @Transient
    private boolean watched;

    public MoovieListContent(){}


    public MoovieListContent(MoovieList moovieList, int mediaId, int customOrder, boolean watched) {
        this.moovieList = moovieList;
        this.mediaId = mediaId;
        this.customOrder = customOrder;
        this.watched = watched;
    }

    public MoovieListContent(MoovieList moovieList, Media media, int mediaId, int customOrder, boolean watched) {
        this.moovieList = moovieList;
        this.mediaId = mediaId;
        this.customOrder = customOrder;
        this.watched = watched;
    }

    public MoovieListContent(MoovieList updatedMoovieList, Integer mediaId, Integer maxCustomOrder) {
        this.moovieList = updatedMoovieList;
        this.mediaId = mediaId;
        this.customOrder = maxCustomOrder + 1;
    }

    public int getId() {
        return id;
    }

    public void setMoovieList(MoovieList moovieList) {
        this.moovieList = moovieList;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public MoovieList getMoovieList() {
        return moovieList;
    }


    public int getCustomOrder() {
        return customOrder;
    }

    public boolean isWatched() {
        return watched;
    }
}