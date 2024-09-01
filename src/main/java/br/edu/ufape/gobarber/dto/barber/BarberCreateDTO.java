package br.edu.ufape.gobarber.dto.barber;

import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarberCreateDTO {

    private String email;
    private String password;
    private String name;
    private String cpf;
    private String contato;
    private String start;
    private String end;
    private AddressCreateDTO address;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;
    private List<Integer> idServices;

}
