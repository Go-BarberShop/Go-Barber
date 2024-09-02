package br.edu.ufape.gobarber.dto.appointment;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentCreateDTO {

    private String clientName;
    private String clientNumber;
    private Integer barberId;
    private List<Integer> serviceTypeIds;
    private String startTime;
}
