package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserService userService;

    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_NON_EXPIRED = true;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            ar.edu.itba.paw.models.User.User user = userService.findUserByUsername(username);
            final Collection<GrantedAuthority> authorities = new ArrayList<>();

            // El user siempre va a tener un UserRole definido. Por lo tanto no deberia haber una NullPointerException
            UserRoles userRole = UserRoles.getRoleFromInt(user.getRole());
            if (userRole == null) {
                throw new UsernameNotFoundException("User role not found for role value: " + user.getRole());
            }

//            HAY que agregar el ROLE_ porque el antMatcher en WebAuthConfig precede los roles con ROLE_
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.name()));

            boolean enabled = user.getRole() != UserRoles.BANNED.getRole();
            boolean accountNonLocked = user.getRole() > UserRoles.NOT_AUTHENTICATED.getRole();

            return new User(username, user.getPassword(), enabled, ACCOUNT_NON_EXPIRED, CREDENTIALS_NON_EXPIRED, accountNonLocked, authorities);

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }

}
