package br.edu.ufape.gobarber.dto.barber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BarberServiceDTO {

    private Integer idBarber;

    private List<Integer> idServices;
}
