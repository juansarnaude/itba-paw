package ar.edu.itba.paw.models.User;

import javax.persistence.*;

@Entity
@Table(name = "userimages", uniqueConstraints = @UniqueConstraint(columnNames = {"userId"}))
public class Image {
    @Id
    private int userId;
    @Column(name = "image")
    private byte[] image;

    public Image(){

    }

    public Image(int userId, byte[] image) {
        this.userId = userId;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public byte[] getImage() {
        return image;
    }
}