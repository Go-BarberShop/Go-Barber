package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository <Barber, Integer> {

}
