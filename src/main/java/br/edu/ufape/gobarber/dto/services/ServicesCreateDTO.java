package br.edu.ufape.gobarber.dto.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServicesCreateDTO {
    private String name;
    private String description;
    private double value;
    private Integer time;
}
