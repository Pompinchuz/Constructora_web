// ============================================
// JWT SERVICE
// ============================================

// JwtService.java
package com.constructora.backend.service;

import com.constructora.backend.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private Long jwtExpiration;
    
    /**
     * Genera un token JWT para un usuario
     */
    public String generarToken(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", usuario.getId());
        extraClaims.put("tipoUsuario", usuario.getTipoUsuario().name());
        extraClaims.put("correo", usuario.getCorreoElectronico());
        
        return generarToken(extraClaims, usuario.getCorreoElectronico());
    }
    
    /**
     * Genera token con claims personalizados
     */
    public String generarToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Extrae el username (correo) del token
     */
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrae el userId del token
     */
    public Long extraerUserId(String token) {
        return extraerClaim(token, claims -> claims.get("userId", Long.class));
    }
    
    /**
     * Extrae el tipo de usuario del token
     */
    public String extraerTipoUsuario(String token) {
        return extraerClaim(token, claims -> claims.get("tipoUsuario", String.class));
    }
    
    /**
     * Extrae un claim específico del token
     */
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Valida si el token es válido para un usuario
     */
    public boolean esTokenValido(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername())) && !esTokenExpirado(token);
    }
    
    /**
     * Valida si el token es válido
     */
    public boolean esTokenValido(String token) {
        try {
            return !esTokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica si el token ha expirado
     */
    private boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }
    
    /**
     * Extrae la fecha de expiración del token
     */
    private Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrae todos los claims del token
     * Actualizado para JJWT 0.12.x
     */
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Obtiene la clave de firma
     * Actualizado para usar SecretKey en lugar de Key
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Obtiene el tiempo de expiración en segundos
     */
    public Long getExpiracion() {
        return jwtExpiration / 1000; // Convertir a segundos
    }
    
    /**
     * Refresca un token (genera uno nuevo con los mismos datos)
     */
    public String refrescarToken(String token) {
        final Claims claims = extraerTodosLosClaims(token);
        String username = claims.getSubject();
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", claims.get("userId"));
        extraClaims.put("tipoUsuario", claims.get("tipoUsuario"));
        extraClaims.put("correo", claims.get("correo"));
        
        return generarToken(extraClaims, username);
    }
}