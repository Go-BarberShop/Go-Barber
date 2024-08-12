package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.SaleControllerDoc;
import br.edu.ufape.gobarber.dto.page.PageSaleDTO;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Integer id) {

        saleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageSaleDTO> getAllSales(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10")Integer size){
        return new ResponseEntity<>(saleService.getAllSales(page, size), HttpStatus.OK);
    }

    @GetMapping("/valid")
    public ResponseEntity<PageSaleDTO> getAllValidSales(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "10")Integer size){
        return new ResponseEntity<>(saleService.getAllValidSales(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSale(@PathVariable Integer id) throws DataBaseException {
        return new ResponseEntity<>(saleService.getSale(id), HttpStatus.OK);
    }

    @GetMapping("/coupon/{coupon}")
    public ResponseEntity<SaleDTO> getSaleByCoupon(@PathVariable String coupon) throws DataBaseException {
        return new ResponseEntity<>(saleService.getSaleByCoupon(coupon), HttpStatus.OK);
    }

}
