package br.edu.ufape.gobarber.dto.barber;

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
    private String name;
    private String cpf;
    private Integer addressId;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;
}
