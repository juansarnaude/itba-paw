package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceImplTest {
    private User user;
    private static final int UID = 1;
    private static final String EMAIL = "test@mail.com";
    private static final String USERNAME = "tester";
    private static final String PASSWORD = "pass123";
    private static final int ROLE = 1;
    private static final String TOKEN = "token";
    private static final int EXPIRATION = 1;
    private static LocalDateTime expiryDate;

    @InjectMocks
    private final VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl();

    @Mock
    private VerificationTokenDao mockVerificationTokenDao;

    @Before
    public void setup(){
        user = new User.Builder(USERNAME,EMAIL,PASSWORD,ROLE,0).userId(UID).build();
    }

    @Test
    public void testCreateVerificationToken() {
        String generatedToken = verificationTokenService.createVerificationToken(user.getUserId());
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockVerificationTokenDao, times(1))
                .createVerificationToken(eq(user.getUserId()), tokenCaptor.capture(), any());
        String tokenCaptured = tokenCaptor.getValue();
        Assert.assertNotNull("Generated token should not be null", tokenCaptured);
        try {
            UUID uuid = UUID.fromString(tokenCaptured);
            Assert.assertNotNull(uuid);
        } catch (IllegalArgumentException e) {
            Assert.fail("Generated token is not a valid UUID.");
        }
    }

    @Test
    public void testIsValidToken(){
        expiryDate = LocalDateTime.now().plusDays(EXPIRATION);
        Token token = new Token(UID,TOKEN,expiryDate);
        Assert.assertTrue(verificationTokenService.isValidToken(token));
    }

    @Test
    public void testRenewToken(){
        expiryDate = LocalDateTime.now().plusDays(EXPIRATION);
        Token token = new Token(UID,TOKEN,expiryDate);
        verificationTokenService.renewToken(token);
        Assert.assertTrue(token.getExpirationDate().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testGetToken(){
        Token token = new Token(UID,TOKEN,expiryDate);
        when(mockVerificationTokenDao.getToken(TOKEN)).thenReturn(java.util.Optional.of(token));
        Assert.assertEquals(token,verificationTokenService.getToken(TOKEN).get());
    }

    @Test
    public void testDeleteToken(){
        Token token = new Token(UID,TOKEN,expiryDate);
        verificationTokenService.deleteToken(token);
        verify(mockVerificationTokenDao, times(1)).deleteToken(token);
    }

    @Test
    public void testIsInvalidToken(){
        expiryDate = LocalDateTime.now().minusDays(EXPIRATION);
        Token token = new Token(UID,TOKEN,expiryDate);
        Assert.assertFalse(verificationTokenService.isValidToken(token));
    }

}
