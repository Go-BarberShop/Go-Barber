package br.edu.ufape.gobarber.dto.barber;

import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Services;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BarberWithServiceDTO {

    private Integer idBarber;

    @Schema(description = "Nome do barbeiro", example = "Zé Tramontina")
    private String name;

    @Schema(description = "Email de login", example = "barber@gobarber.com")
    private String email;

    @Schema(description = "Cpf do barbeiro", example = "12345678910")
    private String cpf;

    @Schema(description = "Endereço do barbeiro", example = "Rua dos bobos")
    private Address address;

    @Schema(description = "Salário do barbeiro", example = "100000")
    private double salary;

    @Schema(description = "Data de admissão do barbeiro", example = "2024-09-11")
    private LocalDate admissionDate;

    @Schema(description = "Carga horária do barbeiro", example = "44")
    private Integer workload;

    @Schema(description = "Serviços autorizados para o barbeiro")
    private Set<ServicesDTO> services;

    private String contato;
    private String start;
    private String end;
}
