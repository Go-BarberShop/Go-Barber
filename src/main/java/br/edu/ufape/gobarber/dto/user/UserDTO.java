package br.edu.ufape.gobarber.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer idUsuario;

    private String login;
}
