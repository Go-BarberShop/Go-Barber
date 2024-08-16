package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
