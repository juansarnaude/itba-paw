package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final int EXPIRY_TIME = 7 * 24 * 60 * 60 * 1000; // 1 week (in millis)
    private final Key jwtKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public JwtTokenProvider(Resource jwtKeyResource) throws IOException {
        this.jwtKey = Keys.hmacShaKeyFor(
                FileCopyUtils.copyToString(new InputStreamReader(jwtKeyResource.getInputStream()))
                        .getBytes(StandardCharsets.UTF_8)
        );
        LOGGER.debug("JwtTokenProvider initialized with key from resource: {}", jwtKeyResource.getFilename());
    }

    /**
     * jws: Json Web Signature (https://datatracker.ietf.org/doc/html/rfc7515)
     */
    public UserDetails parseToken(String jws) {
        LOGGER.debug("Parsing token: {}", jws);
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) jwtKey)
                    .build()
                    .parseSignedClaims(jws).getPayload();

            Date expiration = claims.getExpiration();
            LOGGER.debug("Token expiration date: {}", expiration);

            if (new Date(System.currentTimeMillis()).after(expiration)) {
                LOGGER.warn("Token is expired");
                return null;
            }

            final String username = claims.getSubject();
            LOGGER.debug("Token subject (username): {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                LOGGER.warn("UserDetails is null for username: {}", username);
            } else {
                LOGGER.debug("UserDetails loaded for username: {}", username);
            }
            return userDetails;
        } catch (Exception e) {
            LOGGER.error("Error parsing token: {}", jws, e);
            return null;
        }
    }

    public String createToken(User user) {
        LOGGER.debug("Creating token for user: {}", user.getUsername());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorization", UserRoles.getRoleFromInt(user.getRole()).name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(jwtKey)
                .compact();

        LOGGER.debug("Token created: {}", token);
        return "Bearer " + token;
    }
}
