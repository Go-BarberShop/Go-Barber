package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.exceptions.InvalidRoleException;
import br.edu.ufape.gobarber.model.login.Role;
import br.edu.ufape.gobarber.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findRoleByNome(String nome) throws InvalidRoleException{
        return roleRepository.findByNameIgnoreCase(nome).orElseThrow(() -> new InvalidRoleException("Cargo inválido."));
    }
}
