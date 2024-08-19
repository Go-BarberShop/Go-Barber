package br.edu.ufape.gobarber.dto.productStock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockDTO {

    private Integer idStock;
    private String batchNumber;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDate acquisitionDate;
    private Integer idProduct;
}
