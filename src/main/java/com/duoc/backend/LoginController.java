package com.duoc.backend;
import com.duoc.backend.JWTAuthenticationConfig;
import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
    private JWTAuthenticationConfig jwtAuthtenticationConfig;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public LoginController(
            JWTAuthenticationConfig jwtAuthtenticationConfig,
            MyUserDetailsService userDetailsService
    ) {
        this.jwtAuthtenticationConfig = jwtAuthtenticationConfig;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) throws IllegalAccessException {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(loginRequest.username());

        if (!userDetails.getPassword().equals(loginRequest.password())) {
            throw new IllegalAccessException("Invalid login");
        }

        return jwtAuthtenticationConfig.getJWTToken(loginRequest.username());
    }

    public record LoginRequest(String username, String password) {}

}