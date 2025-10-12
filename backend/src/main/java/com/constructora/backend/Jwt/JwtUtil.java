package com.constructora.backend.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mySecretKeyForJWTThatIsAtLeast256BitsLongForHS256Algorithm";
    private final int JWT_EXPIRATION = 1000 * 60 * 60 * 10;
    private final Set<String> blacklistedTokens = new HashSet<>();

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        String role = "admin".equals(username) ? "ADMIN" : "USER";
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("JWT creado para usuario: " + username + " con rol: " + role);
        return token;
    }

    public boolean validateToken(String token, String username) {
        if (blacklistedTokens.contains(token)) {
            System.out.println("Token en blacklist: " + token);
            return false;
        }
        final String extractedUsername = extractUsername(token);
        return (username.equals(extractedUsername) && !isTokenExpired(token));
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        System.out.println("JWT invalidado: " + token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
