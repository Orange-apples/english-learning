package com.cxylm.springboot.util;

import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.constant.ApiConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.reactivex.functions.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtHelper {
    public static final long JWT_TOKEN_VALIDITY = 3 * 30 * 24 * 60 * 60;

    private final AppProperty appProperty;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = getAllClaimsFromToken(token);
        try {
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            if (e instanceof JwtException) {
                 throw (JwtException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(appProperty.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateToken(long userId, int storeId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(String.valueOf(userId)).setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString()).claim(ApiConstant.JWT_CLAIM_STORE_ID, storeId)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, appProperty.getJwtSecret()).compact();
    }

    public String generateManageToken(long userId, String username) {
        return Jwts.builder().setSubject(String.valueOf(userId)).setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString()).claim(ApiConstant.JWT_CLAIM_USERNAME, username)
                .setExpiration(new Date(System.currentTimeMillis() + 6 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, appProperty.getJwtSecret()).compact();
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return !isTokenExpired(token);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                //.setId("1")
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, appProperty.getJwtSecret()).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
