package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.BannedMessageNotFoundException;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.BannedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BannedServiceImpl implements BannedService{
    @Autowired
    BannedDao bannedDao;
    @Autowired
    UserService userService;

    @Transactional(readOnly = true)
    @Override
    public BannedMessage getBannedMessage(int userId) {
        return bannedDao.getBannedMessage(userId).orElseThrow(() -> new BannedMessageNotFoundException("Banned message for user not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getBannedUsers() {
        return bannedDao.getBannedUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public int getBannedCount() {
        return bannedDao.getBannedCount();
    }
}
