package br.edu.ufape.gobarber.security;

import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String TOKEN_PREFIX = "Bearer";

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    private static final String CARGOS_CLAIM = "cargos";

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
        if (token != null) {
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
}