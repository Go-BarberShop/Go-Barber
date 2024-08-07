package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

}
