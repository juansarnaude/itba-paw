package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
    @Autowired
    private VerificationTokenDao verificationTokenDao;

    private static final int EXPIRATION = 1; //days

    @Transactional
    @Override
    public String createVerificationToken(int userId) {
        String token = UUID.randomUUID().toString();
        verificationTokenDao.createVerificationToken(userId,token,calculateExpirationDate(EXPIRATION));
        return token;
    }

    private LocalDateTime calculateExpirationDate(int expirationTimeInDays){
        return LocalDateTime.now().plusDays(expirationTimeInDays);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Token> getToken(String token) {
        return verificationTokenDao.getToken(token);
    }

    @Transactional
    @Override
    public void deleteToken(Token token) {
        verificationTokenDao.deleteToken(token);
    }
    
    @Override
    public boolean isValidToken(Token token) {
        return token.getExpirationDate().isAfter(LocalDateTime.now());
    }

    @Transactional
    @Override
    public void renewToken(Token token) {
        token.setExpirationDate(calculateExpirationDate(EXPIRATION));
    }
}
