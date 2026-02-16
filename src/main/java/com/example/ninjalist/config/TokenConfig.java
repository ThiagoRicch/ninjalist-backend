package com.example.ninjalist.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ninjalist.model.UserModel;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    Algorithm algorithm = Algorithm.HMAC256("secret");

    public String generateToken(UserModel usermodel) {
        return JWT.create()
                .withClaim("userId", usermodel.getId())
                .withSubject(usermodel.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> verifyToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256("secret");

            DecodedJWT jwt = JWT.require(algorithm)
                    .build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .userId(jwt.getClaim("userId").asLong())
                    .email(jwt.getSubject()).build());

        }
        catch (JWTVerificationException ex ){
            return Optional.empty();
        }
    }
}
