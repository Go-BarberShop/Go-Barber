package br.edu.ufape.gobarber.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressCreateDTO {

    private String street;
    private Integer number;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;
}
