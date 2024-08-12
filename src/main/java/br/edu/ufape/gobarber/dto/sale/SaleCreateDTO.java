package br.edu.ufape.gobarber.dto.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleCreateDTO {

    @NotBlank
    @Schema(description = "Nome descritivo do cupom", example = "Oferta na Progressiva")
    private String name;

    @Positive
    @Schema(description = "Preço promotional", example = "59.99")
    private double totalPrice;

    @Schema(description = "Data de inicio da promoção", example = "2024-08-11")
    private LocalDate startDate;

    @Future
    @Schema(description = "Data de encerramento da promoção", example = "2024-09-11")
    private LocalDate endDate;

    @Schema(description = "Cupom de desconto de 7 caracteres", example = "PR12B02")
    private String coupon;
}
