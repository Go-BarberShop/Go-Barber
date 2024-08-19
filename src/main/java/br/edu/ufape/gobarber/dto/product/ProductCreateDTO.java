package br.edu.ufape.gobarber.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDTO {
    private String nameProduct;
    private String brandProduct;
    private double priceProduct;
    private String size;
}
