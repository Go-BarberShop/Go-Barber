package br.edu.ufape.gobarber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "name_product")
    private String nameProduct;

    @Column(name = "brand_product")
    private String brandProduct;

    @Column(name = "description")
    private String descriptionProduct;

    @Column(name = "price_product")
    private double priceProduct;

    @Column(name = "size")
    private String size;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductStock> productStocks;

    public Product(Integer id, String nameProduct, String brandProduct, String descriptionProduct, double priceProduct, String size) {
        this.idProduct = id;
        this.nameProduct = nameProduct;
        this.brandProduct = brandProduct;
        this.descriptionProduct = descriptionProduct;
        this.priceProduct = priceProduct;
        this.size = size;
    }

}