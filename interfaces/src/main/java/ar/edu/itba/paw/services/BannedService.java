package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.User;

import java.util.List;

public interface BannedService {
    BannedMessage getBannedMessage(int userId);

    List<User> getBannedUsers();

    int getBannedCount();
}
