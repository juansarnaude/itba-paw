package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UnableToCreateUserException;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int UID = 1;
    private static final String EMAIL = "test@mail.com";
    private static final String USERNAME = "tester";
    private static final String PASSWORD = "pass123";
    private static final int ROLE = 1;
    private static final String TOKEN = "token";
    private static final LocalDateTime EXPIRY_DATE = LocalDateTime.now();

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao mockUserDao;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private EmailService mockEmailService;

    @Mock
    private VerificationTokenService mockVerificationTokenService;

    private User user;

    @Before
    public void setup(){
        user = new User.Builder(USERNAME, EMAIL, PASSWORD, ROLE, 0).build();
    }

    @Test
    public void testCreateUserFromUnregistered() {
        when(mockUserDao.createUserFromUnregistered(eq(EMAIL), eq(USERNAME), eq(PASSWORD))).thenReturn(user);
        when(mockPasswordEncoder.encode(anyString())).thenReturn(PASSWORD);
        User user = userService.createUserFromUnregistered(EMAIL, USERNAME, PASSWORD);
        Assert.assertNotNull(user);
        Assert.assertEquals(EMAIL, user.getEmail());
    }

    @Test(expected = UnableToCreateUserException.class)
    public void testCreateUserWithUsedMail(){
        final User realUser = new User.Builder(null, EMAIL, null, ROLE, 0).build();
        final User user = spy(realUser);
        when(mockUserDao.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        final String result = userService.createUser(USERNAME, EMAIL, PASSWORD);
    }

    @Test(expected = UnableToCreateUserException.class)
    public void testCreateExistingUser(){
        final User existingUser = new User.Builder(USERNAME, EMAIL, PASSWORD, ROLE, 0).build();
        when(mockUserDao.findUserByEmail(EMAIL)).thenReturn(Optional.of(existingUser));

        final String token = userService.createUser(USERNAME, EMAIL, PASSWORD);
    }

    @Test(expected = UnableToCreateUserException.class)
    public void testCreateUserWithNullPassword(){
        final String password = null;
        final String result = userService.createUser(USERNAME, EMAIL, password);
    }
}
