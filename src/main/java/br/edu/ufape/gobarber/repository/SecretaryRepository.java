package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Secretary;
import br.edu.ufape.gobarber.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretaryRepository extends JpaRepository <Secretary, Integer> {

    Optional<Secretary> findByUser(User user);
}
