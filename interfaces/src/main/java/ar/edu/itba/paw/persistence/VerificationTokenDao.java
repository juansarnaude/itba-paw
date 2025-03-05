package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Token;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface VerificationTokenDao {
    void createVerificationToken(int userId, String token, LocalDateTime expirationDate);

    Optional<Token> getToken(String token);

    void deleteToken(Token token);

}
