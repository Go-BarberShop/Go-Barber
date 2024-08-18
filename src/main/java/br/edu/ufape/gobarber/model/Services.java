package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Long idService;

    @Column(name = "name_service")
    private String nameService;

    @Column(name = "description_service")
    private String descriptionService;

    @Column(name = "value_service")
    private double valueService;

    @Column(name = "time_service")
    private Integer timeService;

}
