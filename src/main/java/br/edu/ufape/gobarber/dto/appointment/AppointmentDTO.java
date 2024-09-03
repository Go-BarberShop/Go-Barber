package br.edu.ufape.gobarber.dto.appointment;

import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentDTO {

    private Integer id;

    private String clientName;

    private String clientNumber;

    private BarberWithServiceDTO barber;

    private Set<Services> serviceType;

    private String startTime;

    private String endTime;

    private Double totalPrice;
}
