package br.edu.ufape.gobarber.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Integer idProduct;

    @Schema(description = "Nome do produto", example = "Pomada capilar")
    private String nameProduct;

    @Schema(description = "Marca do produto", example = "For men")
    private String brandProduct;

    @Schema(description = "Preço do produto", example = "29")
    private double priceProduct;

    @Schema(description = "Tamanho do produto", example = "50g")
    private String size;
}
