package br.edu.ufape.gobarber.security;

import br.edu.ufape.gobarber.model.login.InvalidTokens;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.repository.InvalidTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String CARGOS_CLAIM = "cargos";
    private final InvalidTokenRepository invalidTokenRepository;

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String gerarTokenJwt(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(expiration));

        String role = user.getRole().getAuthority();

        return TOKEN_PREFIX + " " +
                Jwts.builder()
                        .setIssuer("vemser-api")
                        .claim(Claims.ID, user.getIdUser().toString())
                        .claim(CARGOS_CLAIM, role)
                        .setIssuedAt(now)
                        .setExpiration(exp)
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token != null && invalidTokenRepository.findByTokenContains(token) == null) {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            String user = body.get(Claims.ID, String.class);
            if (user != null) {
                String role = body.get(CARGOS_CLAIM, String.class);
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        }
        return null;
    }

    public void invalidateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        InvalidTokens invalidToken = new InvalidTokens();
        invalidToken.setToken(token);
        invalidToken.setExpiration(getExpirationDateFromToken(token));

        invalidTokenRepository.save(invalidToken);
    }

    @Async
    @Scheduled(fixedRate = 1800000)
    protected void deleteExpiredTokens() {
        List<InvalidTokens> tokens = invalidTokenRepository.findAllByExpirationBefore(LocalDateTime.now());

        invalidTokenRepository.deleteAll(tokens);

    }

    private LocalDateTime getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        Date expirationDate = claims.getExpiration();

        return LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());
    }

}
