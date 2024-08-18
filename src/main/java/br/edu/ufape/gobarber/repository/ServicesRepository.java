package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesRepository extends JpaRepository <Services, Integer> {
    boolean existsByNameService(String nameService);
}
