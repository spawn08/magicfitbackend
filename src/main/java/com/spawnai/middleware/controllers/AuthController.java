package com.spawnai.middleware.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import com.spawnai.middleware.config.JwtTokenProvider;
import com.spawnai.middleware.interfaces.UserRepository;
import com.spawnai.middleware.models.User;
import com.spawnai.middleware.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    private CustomUserDetailsService userService;


    /*
    Login User API -
    This is API is used to login a user. This returns a token for authorization.
    This is POST METHOD call which accepts emailId, password(encrypted), and checks for user authorization.
    MongoDB configuration is inside resources/application.properties
    */

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody data) {
        try {
            String email = data.getEmail();
            String token = "";
            User userExists = userService.findUserByEmail(email);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            if (userExists.getToken() == null || userExists.getToken().isEmpty()) {
                token = jwtTokenProvider.createToken(email, this.users.findByEmail(email).getRoles());
                userExists.setToken(token);
                users.save(userExists);
            } else {
                token = userExists.getToken();
                users.save(userExists);
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("emailId", email);
            model.put("token", token);
            model.put("username", userExists.getUsername());
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    /*
    Register User API -
    This is API is used to register a user.
    This is POST METHOD call which accepts emailId, password(encrypted),
    and username, and stores them into MongoDB.
    MongoDB configuration is inside resources/application.properties
    */

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("message", "User is already registered");
            map.put("status", "success");
            //map.put("id", userExists.getId());
            map.put("username", userExists.getUsername());
            map.put("email", userExists.getEmail());
            return ok(map);
        }
        userService.saveUser(user);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User registered successfully");
        model.put("email", user.getEmail());
        model.put("username", user.getUsername());
        return ok(model);
    }
}
