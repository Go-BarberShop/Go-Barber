package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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

}
