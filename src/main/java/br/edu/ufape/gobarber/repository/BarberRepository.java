package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarberRepository extends JpaRepository <Barber, Integer> {

    Optional<Barber> findByUser(User user);
}
