package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.config.AuthProperties;
import fifth.year.backendinternetapplication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final AuthProperties authProperties;

    public JWTService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public String generateToken(UserDetails userDetails)
    {
        return buildToken(new HashMap<>(), userDetails, authProperties.expiration_token());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, authProperties.expiration_refresh_token());
    }

    public String generateToken
            (
                    Map<String, Object> extraClaims,
                    UserDetails userDetails
            )
    {
        return buildToken(extraClaims, userDetails, authProperties.expiration_token());
    }

    private String buildToken
            (
                    Map<String, Object> extraClaims,
                    UserDetails userDetails,
                    long expiration
            )
    {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSingKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return  extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSingKey(){
        byte[] key = Decoders.BASE64.decode(authProperties.secrete_key());
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers){
        final Claims claims=extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSingKey()).build().parseSignedClaims(token).getPayload();
    }
}
