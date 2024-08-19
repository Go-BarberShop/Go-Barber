package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) throws DataBaseException {
        ProductDTO productDTO = productService.createProduct(productCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductCreateDTO productCreateDTO) throws DataBaseException {
        ProductDTO productDTO = productService.updateProduct(id, productCreateDTO);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) throws DataBaseException {
        productService.deleteProduct(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer id) throws DataBaseException {
        ProductDTO product = productService.getProduct(id);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageProductDTO> getAllProducts(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){

        PageProductDTO products = productService.getAllProducts(page, size);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
