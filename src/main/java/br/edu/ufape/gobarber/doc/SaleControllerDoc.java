package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.sale.SaleCreateDTO;
import br.edu.ufape.gobarber.dto.sale.SaleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseConstraintException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Operation(summary = "Criar promoção", description = "Cria uma promoção, o campo coupon pode ser preenchido com uma String de 7 Caracteres para cadastrar um cupom personalizado ou pode ser deixado como null para que seja gerado um cupom pelo sistema.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Promoção criada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SaleDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Erro na validação do cupom na base de dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object"
                                    ),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "timestamp": "2024-08-12T01:13:15.712+00:00",
                                                      "status": 409,
                                                      "message": "Cupom já cadastrado no banco de dados."
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "object"),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "timestamp": "2024-08-12T01:31:25.745+00:00",
                                                      "status": 400,
                                                      "errors": [
                                                        "name: não deve estar em branco",
                                                        "totalPrice: deve ser maior que 0"
                                                      ]
                                                    }"""
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleCreateDTO saleCreateDTO) throws DataBaseException, DataBaseConstraintException;


    @Operation(summary = "Atualizar promoção", description = "Atualiza uma promoção.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Promoção atualizada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SaleDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Erro na validação do cupom na base de dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object"
                                    ),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "timestamp": "2024-08-12T01:13:15.712+00:00",
                                                      "status": 409,
                                                      "message": "Cupom já cadastrado no banco de dados."
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "object"),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "timestamp": "2024-08-12T01:31:25.745+00:00",
                                                      "status": 400,
                                                      "errors": [
                                                        "name: não deve estar em branco",
                                                        "totalPrice: deve ser maior que 0"
                                                      ]
                                                    }"""
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@Valid @RequestBody SaleCreateDTO saleCreateDTO, @PathVariable Integer id) throws DataBaseException;
}
