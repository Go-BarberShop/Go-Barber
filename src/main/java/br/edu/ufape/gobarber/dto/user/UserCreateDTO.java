package br.edu.ufape.gobarber.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    @NotNull
    @Schema(description = "Login do usuário no sistema", example = "José")
    private String login;

    @NotNull
    @Schema(description = "Senha do usuário no sistema", example = "123")
    private String password;

    @NotNull
    @Schema(description = "Cargo do usuário no sistema", example = "[\"ADMIN\",\"CUSTOMER\",\"EMPLOYEE\"]")
    private String role;
}
