package br.edu.ufape.gobarber.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressCreateDTO {

    @NotBlank(message = "Rua é obrigatória")
    private String street;
    private Integer number;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    private String state;
    private String cep;
}
