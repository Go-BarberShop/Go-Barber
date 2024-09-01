package br.edu.ufape.gobarber.dto.secretary;

import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretaryCreateDTO {

    private String email;
    private String password;
    private String name;
    private String cpf;
    private String contact;
    private String start;
    private String end;
    private AddressCreateDTO address;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;

}
