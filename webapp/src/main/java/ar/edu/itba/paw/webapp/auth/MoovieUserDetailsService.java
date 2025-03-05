package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MoovieUserDetailsService implements UserDetailsService {

    private UserService us;

    private boolean ACCOUNT_ENABLED = true;
    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_NON_EXPIRED = true;
    private boolean ACCOUNT_NON_LOCKED = true;

    @Autowired
    public MoovieUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        ACCOUNT_ENABLED = true;
        ACCOUNT_NON_LOCKED = true;

        final User user = us.findUserByUsername(username);

        final Set<GrantedAuthority> authorities = new HashSet<>();

        if (user.getRole() == 1 || user.getRole()==2 ) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if(user.getRole() == 2 ){
                authorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            }
        } else {
            if(user.getRole() == -2  ){
                ACCOUNT_NON_LOCKED = false;
            }
            if(user.getRole() == -1 ){
                ACCOUNT_ENABLED = false;
            }
            // If the user doesn't have the required role, you can handle it here.
        }

        return new MoovieAuthUser(user.getUsername(), user.getPassword(), ACCOUNT_ENABLED, ACCOUNT_NON_EXPIRED, CREDENTIALS_NON_EXPIRED, ACCOUNT_NON_LOCKED, authorities);
    }
}
