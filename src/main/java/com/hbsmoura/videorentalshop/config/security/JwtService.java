package com.hbsmoura.videorentalshop.config.security;

import com.hbsmoura.videorentalshop.enums.EnumUserRole;
import com.hbsmoura.videorentalshop.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "39792442264529482B4D6251655468576D5A7134743777217A25432A462D4A614E635266556A586E3272357538782F413F4428472B4B6250655367566B597033";
    private static final long DEFAULT_EXPIRATION = 1000L * 60 * 60;

    public String generateToken(User user) {
        Date currentTime = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(new Date(currentTime.getTime() + DEFAULT_EXPIRATION))
                .claim("name", user.getName())
                .claim("authorities", user.getAuthorities())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Collection<EnumUserRole> extractAuthorities(String token) {
        Collection<String> stringAuthorities = extractClaims(token).get("authorities", Collection.class);
        return stringAuthorities.stream().map(EnumUserRole::valueOf).toList();
    }

    public boolean isUsefulToken(String token) {
        return isValidToken(token) && !isExpiredToken(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isExpiredToken(String token) {
        return extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
