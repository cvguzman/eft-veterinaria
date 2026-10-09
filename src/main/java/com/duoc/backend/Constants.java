package com.duoc.backend;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String SUPER_SECRET_KEY = cargarClaveJWT();

    private static String cargarClaveJWT() {
        String clave = System.getenv("JWT_SECRET");

        if (clave == null || clave.isBlank()) {
            throw new IllegalStateException(
                    "Debe configurar la variable JWT_SECRET"
            );
        }

        return clave;
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}