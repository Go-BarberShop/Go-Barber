package br.edu.ufape.gobarber.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDateTime startTime;
}
