package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Token;

import java.util.Optional;

public interface VerificationTokenService {
    String createVerificationToken(int userId);

    Optional<Token> getToken(String token);

    void deleteToken(Token token);

    boolean isValidToken(Token token);

    void renewToken(Token token);
}
