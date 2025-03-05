package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "moovielistsfollows",uniqueConstraints = @UniqueConstraint(columnNames = {"moovieListId", "userId"}))
public class MoovieListFollowers implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "moovieListId", referencedColumnName = "moovieListId")
    private MoovieList moovieList;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public MoovieListFollowers() {
    }

    public MoovieListFollowers(MoovieList moovieList, User user) {
        this.moovieList = moovieList;
        this.user = user;
    }

    // Getters and setters
    public MoovieList getMoovieList() {
        return moovieList;
    }

    public void setMoovieList(MoovieList moovieList) {
        this.moovieList = moovieList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
