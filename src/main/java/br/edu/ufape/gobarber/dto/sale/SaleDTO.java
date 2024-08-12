package br.edu.ufape.gobarber.dto.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleDTO {

    private Integer id;

    @Schema(description = "Nome descritivo do cupom", example = "Oferta na Progressiva")
    private String name;

    @Schema(description = "Preço promotional", example = "59.99")
    private double totalPrice;

    @Schema(description = "Cupom de desconto de 7 caracteres", example = "PROGRES")
    private String coupon;

    @Schema(description = "Data de inicio da promoção", example = "2024-08-11")
    private LocalDate startDate;

    @Schema(description = "Data de encerramento da promoção", example = "2024-09-11")
    private LocalDate endDate;
}
