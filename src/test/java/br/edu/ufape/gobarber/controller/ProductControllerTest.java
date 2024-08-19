package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductCreateDTO productCreateDTO;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productCreateDTO = new ProductCreateDTO("Pomada", "Marca X", "Para todos os tipos de cabelo", 29.99, "50g");
        productDTO = new ProductDTO(1, "Pomada", "Marca X", "Para todos os tipos de cabelo", 29.99, "50g");
    }

    @Test
    void testCreateProduct() throws DataBaseException {
        when(productService.createProduct(any(ProductCreateDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(productCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    void testUpdateProduct() throws DataBaseException {
        when(productService.updateProduct(eq(1), any(ProductCreateDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(1, productCreateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).updateProduct(eq(1), any(ProductCreateDTO.class));
    }

    @Test
    void testDeleteProduct() throws DataBaseException {
        doNothing().when(productService).deleteProduct(1);

        ResponseEntity<Void> response = productController.deleteProduct(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testGetProduct() throws DataBaseException {
        when(productService.getProduct(1)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.getProduct(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).getProduct(1);
    }

    @Test
    void testGetAllProducts() {
        PageProductDTO pageProductDTO = new PageProductDTO(10L, 1, 0, 10, Arrays.asList(productDTO));
        when(productService.getAllProducts(0, 10)).thenReturn(pageProductDTO);

        ResponseEntity<PageProductDTO> response = productController.getAllProducts(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageProductDTO, response.getBody());
        verify(productService, times(1)).getAllProducts(0, 10);
    }

    @Test
    void testCreateProduct_ThrowsException() throws DataBaseException {
        when(productService.createProduct(any(ProductCreateDTO.class))).thenThrow(new DataBaseException("Erro ao criar produto"));

        DataBaseException exception = assertThrows(DataBaseException.class, () -> {
            productController.createProduct(productCreateDTO);
        });

        assertEquals("Erro ao criar produto", exception.getMessage());
        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    void testUpdateProduct_ThrowsException() throws DataBaseException {
        when(productService.updateProduct(eq(1), any(ProductCreateDTO.class))).thenThrow(new DataBaseException("Produto não encontrado"));

        DataBaseException exception = assertThrows(DataBaseException.class, () -> {
            productController.updateProduct(1, productCreateDTO);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productService, times(1)).updateProduct(eq(1), any(ProductCreateDTO.class));
    }

    @Test
    void testDeleteProduct_ThrowsException() throws DataBaseException {
        doThrow(new DataBaseException("Produto não encontrado")).when(productService).deleteProduct(1);

        DataBaseException exception = assertThrows(DataBaseException.class, () -> {
            productController.deleteProduct(1);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testGetProduct_ThrowsException() throws DataBaseException {
        when(productService.getProduct(1)).thenThrow(new DataBaseException("Produto não encontrado"));

        DataBaseException exception = assertThrows(DataBaseException.class, () -> {
            productController.getProduct(1);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productService, times(1)).getProduct(1);
    }

    @Test
    void testGetAllProducts_EmptyPage() {
        PageProductDTO emptyPage = new PageProductDTO(0L, 0, 0, 10, Arrays.asList());
        when(productService.getAllProducts(0, 10)).thenReturn(emptyPage);

        ResponseEntity<PageProductDTO> response = productController.getAllProducts(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyPage, response.getBody());
        verify(productService, times(1)).getAllProducts(0, 10);
    }
}
