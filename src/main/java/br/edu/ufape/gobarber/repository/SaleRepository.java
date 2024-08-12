package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    Optional<Sale> findByCoupon(String coupon);
    Page<Sale> findAllByEndDateAfter(LocalDate endDate, Pageable pageable);
}
