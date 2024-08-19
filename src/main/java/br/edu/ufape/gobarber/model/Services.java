package br.edu.ufape.gobarber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "service")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Integer idService;

    @Column(name = "name_service")
    private String nameService;

    @Column(name = "description_service")
    private String descriptionService;

    @Column(name = "price_service")
    private double valueService;

    @Column(name = "time_service")
    private LocalTime timeService;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private Set<Barber> barbers;

}
