package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;

public interface BannedDao {
    Optional<BannedMessage> getBannedMessage(int userId);

    void createBannedMessage(int bannedUserId, int modUserId , String message, String modUsername);

    void deleteBannedMessage(int bannedUserId);

    List<User> getBannedUsers();

    int getBannedCount();
}
