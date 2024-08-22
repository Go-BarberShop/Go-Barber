package br.edu.ufape.gobarber.controller.auth;

import br.edu.ufape.gobarber.dto.user.LoginDTO;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<Map<String, String>> auth(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getLogin(),
                            loginDTO.getPassword()
                    );

            Authentication authentication =
                    authenticationManager.authenticate(
                            usernamePasswordAuthenticationToken);

            User validatedUser = (User) authentication.getPrincipal();

            String jwt = tokenService.gerarTokenJwt(validatedUser);
            jwt = jwt.replace(jwt.substring(0, 7), "");
            String role = validatedUser.getRole().getAuthority();
            role = role.replace(role.substring(0, 5), "");

            Map<String, String> response = new HashMap<>();
            response.put("role", role);
            response.put("token", jwt);

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(Map.of("error", "Email ou senha inválidos"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Falha de Autenticação, Tente Novamente!"), HttpStatus.FORBIDDEN);
        }
    }

}