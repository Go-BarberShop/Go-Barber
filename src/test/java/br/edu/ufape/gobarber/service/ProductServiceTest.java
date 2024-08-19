package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testCreateProduct_Success() throws DataBaseException {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO("Produto A", "Marca A", "Descrição A", 29.99, "100ml");
        Product product = new Product(1, "Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.createProduct(productCreateDTO);

        assertNotNull(result);
        assertEquals("Produto A", result.getNameProduct());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testCreateProduct_DataBaseException() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO("Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        when(productRepository.save(any(Product.class))).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> productService.createProduct(productCreateDTO));
    }

    @Test
    public void testUpdateProduct_Success() throws DataBaseException {
        ProductCreateDTO updatedProductDTO = new ProductCreateDTO("Produto B", "Marca B", "Descrição B", 39.99, "200ml");
        Product existingProduct = new Product(1, "Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductDTO result = productService.updateProduct(1, updatedProductDTO);

        assertNotNull(result);
        assertEquals("Produto B", result.getNameProduct());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        ProductCreateDTO updatedProductDTO = new ProductCreateDTO("Produto B", "Marca B", "Descrição B", 39.99, "200ml");

        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> productService.updateProduct(1, updatedProductDTO));
    }

    @Test
    public void testDeleteProduct_Success() throws DataBaseException {
        Product existingProduct = new Product(1, "Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(1);

        verify(productRepository, times(1)).delete(existingProduct);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> productService.deleteProduct(1));
    }

    @Test
    public void testGetProduct_Success() throws DataBaseException {
        Product existingProduct = new Product(1, "Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        ProductDTO result = productService.getProduct(1);

        assertNotNull(result);
        assertEquals("Produto A", result.getNameProduct());
    }

    @Test
    public void testGetProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> productService.getProduct(1));
    }

    @Test
    public void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product(1, "Produto A", "Marca A", "Descrição A", 29.99, "100ml");
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        PageProductDTO result = productService.getAllProducts(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testConvertDTOtoEntity() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO("Produto A", "Marca A", "Descrição A", 29.99, "100ml");

        Product result = productService.convertDTOtoEntity(productCreateDTO);

        assertNotNull(result);
        assertEquals("Produto A", result.getNameProduct());
        assertEquals("Marca A", result.getBrandProduct());
        assertEquals("Descrição A", result.getDescriptionProduct());
    }
}
