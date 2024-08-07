package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.SaleControllerDoc;
import br.edu.ufape.gobarber.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@Validated
@Slf4j
public class SaleController implements SaleControllerDoc {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/sale/notify")
    public ResponseEntity<Void> sendSaleEmail(@RequestParam(value = "idSale") Integer idSale)  {

        saleService.sendPromotionalEmail(idSale);
        return ResponseEntity.accepted().build();
    }

}
