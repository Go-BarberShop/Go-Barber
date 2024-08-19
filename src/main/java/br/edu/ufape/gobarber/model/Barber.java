package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "barber")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barber")
    private Integer idBarber;

    @Column(name="name_barber")
    private String name;

    @Column(name = "cpf_barber")
    private String cpf;

    @OneToOne
    @JoinColumn(name = "id_adress", referencedColumnName = "id_adress")
    private Address address;

    @Column(name = "salary")
    private double salary;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "workload")
    private Integer workload;

    @ManyToMany
    @JoinTable(
            name = "barber_x_service", // Name of the join table
            joinColumns = @JoinColumn(name = "id_barber"), // Foreign key for Barber in join table
            inverseJoinColumns = @JoinColumn(name = "id_service") // Foreign key for Service in join table
    )
    private Set<Services> services = new HashSet<>();

    public void addService(Services services){
        this.services.add(services);
    }

    public void removeService(Services services) {
        this.services.remove(services);
    }
}
