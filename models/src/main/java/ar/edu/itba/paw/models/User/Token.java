package ar.edu.itba.paw.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "verificationTokens")
public class Token {

    //Ver cómo traer el userId
    @Id
    private int userId;

    @Column(length = 100, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    //Para hibernate
    public Token(){

    }

    public Token(int userId, String token, LocalDateTime expirationDate) {
        this.userId = userId;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
