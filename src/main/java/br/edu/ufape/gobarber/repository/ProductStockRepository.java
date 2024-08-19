package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.model.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository <ProductStock, Integer> {
    Page<ProductStock> findByProduct(Product product, Pageable pageable);
}
