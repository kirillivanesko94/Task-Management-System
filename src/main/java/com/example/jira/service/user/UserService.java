package com.example.jira.service.user;

import com.example.jira.common.*;
import com.example.jira.db.user.*;
import com.example.jira.scurity.User;
import com.example.jira.scurity.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder pwdEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtility;

    public UserService(
        UserRepository repository,
        PasswordEncoder pwdEncoder,
        @Lazy AuthenticationManager authenticationManager,
        JwtTokenUtil jwtUtility
    ) {
        this.repository = repository;
        this.pwdEncoder = pwdEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
    }

    public void registerNewUser(String name, String login, String pwd) {
        PlatformUser newUser = new PlatformUser();
        newUser.setId(UUID.randomUUID());
        newUser.setName(name);
        newUser.setLogin(login);
        newUser.setPassword(pwdEncoder.encode(pwd));

        repository.insert(newUser);
    }

    public String login(String login, String pwd) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, pwd));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtUtility.generateToken(login, Collections.emptyList());
    }

    public Optional<PlatformUser> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PlatformUser user = repository
            .findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login %s not found in DB", username)));

        return platformUserToSecurityUser(user);
    }

    private User platformUserToSecurityUser(PlatformUser user) {
        return new User(
            user.getId(),
            user.getLogin(),
            user.getPassword()
        );
    }
}
