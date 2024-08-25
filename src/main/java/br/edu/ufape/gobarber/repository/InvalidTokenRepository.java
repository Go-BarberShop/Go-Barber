package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.login.InvalidTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvalidTokenRepository extends JpaRepository<InvalidTokens, Long> {
    InvalidTokens findByTokenContains(String token);
    List<InvalidTokens> findAllByExpirationBefore(LocalDateTime now);
}
