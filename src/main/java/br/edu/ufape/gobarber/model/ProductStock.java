package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product_stock")
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Integer idStock;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
}
