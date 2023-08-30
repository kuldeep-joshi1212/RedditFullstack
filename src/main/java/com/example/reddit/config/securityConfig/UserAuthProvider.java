package com.example.reddit.config.securityConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.reddit.model.User;
import com.example.reddit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private UserRepository userRepository;
    @Value("${security.jwt.token.secret-key:secret-key:secret}")
    private String seceretKey;

    @Autowired
    private UserAuthProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void init() {
        seceretKey = Base64.getEncoder().encodeToString(seceretKey.getBytes());
    }
    public String createToken(User user) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        final int hoursTillTokenValid = 24;
        cal.add(Calendar.HOUR_OF_DAY, hoursTillTokenValid);
        Date validity = cal.getTime();
        return JWT.create()
                .withIssuer(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("Email", user.getEmail())
                .sign(Algorithm.HMAC256(seceretKey));
    }

    public Authentication validateToken(String token) throws RuntimeException {
        Algorithm algorithm = Algorithm.HMAC256(seceretKey);
        JWTVerifier verfier = JWT.require(algorithm).build();
        DecodedJWT decoded = verfier.verify(token);
        User user = userRepository.findByUsername(decoded.getIssuer());
        if (Objects.isNull(user) || !user.getToken().equals(token)) {
            throw new RuntimeException("Token does not belong to this User");
        }
        return  new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
