package br.edu.ufape.gobarber.model;

import br.edu.ufape.gobarber.model.login.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
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

    @Column(name = "contact")
    private String contato;

    @Column(name = "start_working")
    private LocalTime start;

    @Column(name = "end_working")
    private LocalTime end;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

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
