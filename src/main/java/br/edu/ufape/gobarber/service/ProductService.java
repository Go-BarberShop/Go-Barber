package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO createProduct(@Valid ProductCreateDTO productCreateDTO) throws DataBaseException{
        try {
            Product product = convertDTOtoEntity(productCreateDTO);

            product = productRepository.save(product);

            return convertEntityToDTO(product);
        }catch (Exception e) {
            throw new DataBaseException("Erro ao criar produto: " + e.getMessage());
        }
    }

    @Transactional
    public ProductDTO updateProduct(Integer id, ProductCreateDTO updatedProductDTO) throws DataBaseException{
        Product product = productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados."));

        product.setNameProduct(updatedProductDTO.getNameProduct());
        product.setBrandProduct(updatedProductDTO.getBrandProduct());
        product.setPriceProduct(updatedProductDTO.getPriceProduct());
        product.setSize(updatedProductDTO.getSize());

        product = productRepository.save(product);

        return convertEntityToDTO(product);
    }

    @Transactional
    public void deleteProduct(Integer id) throws DataBaseException{
        Product product = productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados."));

        productRepository.delete(product);
    }

    public ProductDTO getProduct(Integer id) throws DataBaseException{
        return convertEntityToDTO(productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados.")));
    }

    public PageProductDTO getAllProducts(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductDTO> productDTOPage = productPage.map(this::convertEntityToDTO);

        return new PageProductDTO(
                productDTOPage.getTotalElements(),
                productDTOPage.getTotalPages(),
                productDTOPage.getPageable().getPageNumber(),
                productDTOPage.getSize(),
                productDTOPage.getContent()
        );
    }

    private Product convertDTOtoEntity(ProductCreateDTO productCreateDTO) {
        Product product = new Product();
        product.setNameProduct(productCreateDTO.getNameProduct());
        product.setBrandProduct(productCreateDTO.getBrandProduct());
        product.setPriceProduct(productCreateDTO.getPriceProduct());
        product.setSize(productCreateDTO.getSize());

        return product;
    }

    private ProductDTO convertEntityToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setNameProduct(product.getNameProduct());
        productDTO.setBrandProduct(product.getBrandProduct());
        productDTO.setPriceProduct(product.getPriceProduct());
        productDTO.setSize(product.getSize());

        return productDTO;
    }
}
