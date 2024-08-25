package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.login.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNameIgnoreCase(String name);
}
