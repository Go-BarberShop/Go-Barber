package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Integer> {

}
