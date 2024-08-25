package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.user.UserCreateDTO;
import br.edu.ufape.gobarber.dto.user.UserDTO;
import br.edu.ufape.gobarber.exceptions.InvalidRoleException;
import br.edu.ufape.gobarber.exceptions.UniqueConstraintViolationException;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }


    public UserDTO create(UserCreateDTO newUser) throws UniqueConstraintViolationException, InvalidRoleException {
        Optional<User> loginUser = userRepository.findByLogin(newUser.getLogin());
        if(loginUser.isPresent()){
            throw new UniqueConstraintViolationException("Usuário já cadastrado com esse email.");
        }
        User user = convertCreateDTOtoEntity(newUser);
        user = userRepository.save(user);

        return convertEntityToDTO(user);
    }

    private UserDTO convertEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUsuario(user.getIdUser());
        userDTO.setLogin(user.getLogin());

        return userDTO;
    }

    private User convertCreateDTOtoEntity(UserCreateDTO newUser) throws InvalidRoleException {
        User user = new User();
        user.setLogin(newUser.getLogin());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(roleService.findRoleByNome("ROLE_" + newUser.getRole()));

        return user;
    }
}

