package br.edu.ufape.gobarber.dto.barber;

import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BarberCreateDTO {

    private String email;
    private String password;
    private String name;
    private String cpf;
    private AddressCreateDTO address;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;

}
