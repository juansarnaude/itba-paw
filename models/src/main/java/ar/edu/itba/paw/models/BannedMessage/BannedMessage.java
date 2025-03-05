package ar.edu.itba.paw.models.BannedMessage;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bannedmessage")
public class BannedMessage {

    @Column(name = "modUserId")
    private int modUserId;

    @Column(nullable = false, length = 100)
    private String modUsername;

    @Column(length = 250)
    private String message;

    @Id
    @Column(name = "banneduserid")
    private int bannedUserId;

    public BannedMessage(){

    }

    public BannedMessage(int modUserId, String modUsername, String message) {
        this.modUserId = modUserId;
        this.modUsername = modUsername;
        this.message = message;
    }

    public int getBannedUserId() {
        return bannedUserId;
    }

    public int getModUserId() {
        return modUserId;
    }

    public String getModUsername() {
        return modUsername;
    }

    public String getMessage() {
        return message;
    }
}
