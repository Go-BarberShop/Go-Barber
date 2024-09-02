package br.edu.ufape.gobarber.dto.secretary;

import br.edu.ufape.gobarber.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SecretaryDTO {

    private Integer idSecretary;

    @Schema(description = "Nome da secretária", example = "Ana Luíza")
    private String name;

    @Schema(description = "Email de login", example = "barber@gobarber.com")
    private String email;

    @Schema(description = "Cpf da secretária", example = "12345678910")
    private String cpf;

    @Schema(description = "Endereço da secretária", example = "Rua Brás Cubas")
    private Address address;

    @Schema(description = "Salário da secretária", example = "3000.00")
    private double salary;

    @Schema(description = "Data de admissão da secretária", example = "2023-07-12")
    private LocalDate admissionDate;

    @Schema(description = "Carga horária da secretária", example = "44")
    private Integer workload;

    private String contact;
    private String start;
    private String end;
}


