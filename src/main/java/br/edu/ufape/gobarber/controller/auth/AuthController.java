package br.edu.ufape.gobarber.controller.auth;

import br.edu.ufape.gobarber.dto.user.LoginDTO;
import br.edu.ufape.gobarber.dto.user.UserCreateDTO;
import br.edu.ufape.gobarber.dto.user.UserDTO;
import br.edu.ufape.gobarber.exceptions.UniqueConstraintViolationException;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.security.TokenService;
import br.edu.ufape.gobarber.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String auth(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getLogin(),
                        loginDTO.getPassword()
                );

        Authentication authentication =
                authenticationManager.authenticate(
                        usernamePasswordAuthenticationToken);

        User validatedUser = (User) authentication.getPrincipal();

        return tokenService.gerarTokenJwt(validatedUser);
    }

}