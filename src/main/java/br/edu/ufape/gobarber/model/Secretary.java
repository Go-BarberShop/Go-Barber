package br.edu.ufape.gobarber.model;

import br.edu.ufape.gobarber.model.login.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "secretary")
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secretary")
    private Integer idSecretary;

    @Column(name = "name_secretary")
    private String name;

    @Column(name = "cpf_secretary")
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
    private String contact;

    @Column(name = "start_working")
    private LocalTime start;

    @Column(name = "end_working")
    private LocalTime end;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
