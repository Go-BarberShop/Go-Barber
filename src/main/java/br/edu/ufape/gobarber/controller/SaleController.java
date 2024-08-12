package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.SaleControllerDoc;
import br.edu.ufape.gobarber.dto.sale.SaleCreateDTO;
import br.edu.ufape.gobarber.dto.sale.SaleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseConstraintException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sale")
@Validated
@Slf4j
public class SaleController implements SaleControllerDoc {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/email/notify")
    public ResponseEntity<Void> sendSaleEmail(@RequestParam(value = "idSale") Integer idSale)  {

        saleService.sendPromotionalEmail(idSale);
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleCreateDTO saleCreateDTO) throws DataBaseException, DataBaseConstraintException {

        SaleDTO saleDTO = saleService.createSale(saleCreateDTO);
        return new ResponseEntity<>(saleDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@Valid @RequestBody SaleCreateDTO saleCreateDTO, @PathVariable Integer id) throws DataBaseException {

        SaleDTO saleDTO = saleService.updateSale(id, saleCreateDTO);
        return new ResponseEntity<>(saleDTO, HttpStatus.OK);
    }
}
