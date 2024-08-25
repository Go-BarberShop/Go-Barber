package br.edu.ufape.gobarber.model.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROLE")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(name = "name_role")
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "EMPLOYEE_X_ROLE",
            joinColumns = @JoinColumn(name = "ID_ROLE"),
            inverseJoinColumns = @JoinColumn(name = "ID_USER")
    )
    private Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
