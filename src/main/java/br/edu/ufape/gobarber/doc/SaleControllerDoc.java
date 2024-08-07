package br.edu.ufape.gobarber.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SaleControllerDoc {

    @Operation(summary = "Notificar sobre promoção", description = "Notifica todos os clientes regitrados por email, a respeito da promoção passada por RequestParam")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "202", description = "Emails enviados com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Falha interna do servidor")
            }
    )
    @PostMapping("/sale/notify")
    public ResponseEntity<Void> sendSaleEmail(@RequestParam(value = "idSale") Integer idSale);
}
