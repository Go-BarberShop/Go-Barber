package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adress")
    private Integer idAddress;

    @NotBlank(message = "Rua é obrigatória")
    @Column(name="street")
    private String street;

    @Column(name = "number")
    private Integer number;

    @NotBlank(message = "Bairro é obrigatório")
    @JoinColumn(name = "neighborhood")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    @Column(name = "state")
    private String state;

    @Column(name = "cep")
    private String cep;

}
