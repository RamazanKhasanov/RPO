package ru.iu3.rpo.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;
import ru.iu3.rpo.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Value("${private.session-timeout}")
    private int sessionTimeout;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)  throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String userName,
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        Object token = usernamePasswordAuthenticationToken.getCredentials();
        Optional<ru.iu3.rpo.backend.models.User> uu = userRepository.findByToken(String.valueOf(token));
        if(!uu.isPresent())
            throw new UsernameNotFoundException("user is not found");
        ru.iu3.rpo.backend.models.User u = uu.get();

        boolean timeout = true;
        LocalDateTime dt = LocalDateTime.now();
        if (u.activity != null)
        {
            LocalDateTime nt = u.activity.plusMinutes(sessionTimeout);
            if (dt.isBefore(nt))
                timeout = false;
        }
        if (timeout) {
            u.token = null;
            userRepository.save(u);
            throw new NonceExpiredException("session is expired");
        }
        else {
            u.activity = dt;
            userRepository.save(u);
        }
        UserDetails user = new User(u.login, u.password,
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("USER"));
        return user;
    }
}
