package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.page.PageStockDTO;
import br.edu.ufape.gobarber.dto.productStock.ProductStockCreateDTO;
import br.edu.ufape.gobarber.dto.productStock.ProductStockDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService productStockService;

    @PostMapping
    public ResponseEntity<ProductStockDTO> createStock(@Valid @RequestBody ProductStockCreateDTO productCreateDTO) throws DataBaseException {
        ProductStockDTO stockDTO = productStockService.createStock(productCreateDTO);

        return new ResponseEntity<>(stockDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductStockDTO> updateStock(@PathVariable Integer id, @Valid @RequestBody ProductStockCreateDTO productStockCreateDTO) throws DataBaseException {
        ProductStockDTO stockDTO = productStockService.updateStock(productStockCreateDTO, id);

        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Integer id) {
        productStockService.deleteStock(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductStockDTO> getStock(@PathVariable Integer id) throws DataBaseException {
        ProductStockDTO stockDTO = productStockService.getStock(id);

        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<PageStockDTO> getStockByProduct(@PathVariable Integer id,
                                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) throws DataBaseException {
        PageStockDTO pageStockDTO = productStockService.getStockByProduct(id, page, size);

        return new ResponseEntity<>(pageStockDTO, HttpStatus.OK);
    }

}
