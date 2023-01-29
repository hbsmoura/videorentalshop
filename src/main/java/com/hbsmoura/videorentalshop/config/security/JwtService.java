package com.hbsmoura.videorentalshop.config.security;

import com.hbsmoura.videorentalshop.enums.EnumUserRole;
import com.hbsmoura.videorentalshop.exceptions.KeyPairException;
import com.hbsmoura.videorentalshop.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtService {
    private static final long DEFAULT_EXPIRATION = 1000L * 60 * 60;

    private final KeyFactory keyFactory;

    public JwtService() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance("RSA");;
    }

    public String generateToken(User user) {
        Date currentTime = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(new Date(currentTime.getTime() + DEFAULT_EXPIRATION))
                .claim("name", user.getName())
                .claim("authorities", user.getAuthorities())
                .signWith(getPrivateKey())
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
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isExpiredToken(String token) {
        return extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private PrivateKey getPrivateKey() {
        String privateKeyPathString = "src/main/resources/certs/private.der";

        try {
            Path privateKeyPath = Paths.get(privateKeyPathString);
            byte[] privateKeyBytes = Files.readAllBytes(privateKeyPath);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (Exception e) {
            throw new KeyPairException();
        }
    }
    private PublicKey getPublicKey() {
        String publicKeyPathString = "src/main/resources/certs/public.der";

        try {
            Path publicKeyPath = Paths.get(publicKeyPathString);
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyPath);
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            throw new KeyPairException();
        }
    }
}
