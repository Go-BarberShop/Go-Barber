package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.page.PageStockDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.dto.productStock.ProductStockCreateDTO;
import br.edu.ufape.gobarber.dto.productStock.ProductStockDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.model.ProductStock;
import br.edu.ufape.gobarber.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockRepository productStockRepository;
    private final ProductService productService;

    @Transactional
    public ProductStockDTO createStock(ProductStockCreateDTO stockCreateDTO) throws DataBaseException {
        try{
            ProductStock productStock = convertDTOtoStock(stockCreateDTO);

            productStock = productStockRepository.save(productStock);

            return convertStockToDTO(productStock);

        } catch (Exception e){
            throw new DataBaseException("Erro ao criar estoque: " + e.getMessage());
        }
    }

    @Transactional
    public ProductStockDTO updateStock(ProductStockCreateDTO stockCreateDTO, Integer id) throws DataBaseException {

        ProductStock productStock = productStockRepository.findById(id)
                                    .orElseThrow(() -> new DataBaseException("Não existe Estoque com o id informado"));

        ProductStock newProductStock = convertDTOtoStock(stockCreateDTO);

        productStock.setQuantity(newProductStock.getQuantity());
        productStock.setExpirationDate(newProductStock.getExpirationDate());
        productStock.setAcquisitionDate(newProductStock.getAcquisitionDate());
        productStock.setBatchNumber(newProductStock.getBatchNumber());
        productStock.setProduct(newProductStock.getProduct());

        productStock = productStockRepository.save(productStock);

        return convertStockToDTO(productStock);
    }

    @Transactional
    public void deleteStock(Integer id) {
        productStockRepository.deleteById(id);
    }

    public ProductStockDTO getStock(Integer id) throws DataBaseException {
        return convertStockToDTO(productStockRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Não existe Estoque com o id informado")));
    }

    public PageStockDTO getStockByProduct(Integer id, Integer page, Integer size) throws DataBaseException {
        Pageable pageable = PageRequest.of(page, size);
        Product product = productService.getProductById(id);

        Page<ProductStock> stockPage = productStockRepository.findByProduct(product, pageable);
        Page<ProductStockDTO> productDTOPage = stockPage.map(this::convertStockToDTO);

        return new PageStockDTO(
                productDTOPage.getTotalElements(),
                productDTOPage.getTotalPages(),
                productDTOPage.getPageable().getPageNumber(),
                productDTOPage.getSize(),
                productDTOPage.getContent()
        );
    }

    private ProductStock convertDTOtoStock(ProductStockCreateDTO stockCreateDTO) throws DataBaseException {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(productService.getProductById(stockCreateDTO.getIdProduct()));
        productStock.setQuantity(stockCreateDTO.getQuantity());
        productStock.setBatchNumber(stockCreateDTO.getBatchNumber());
        productStock.setExpirationDate(stockCreateDTO.getExpirationDate());
        productStock.setAcquisitionDate(stockCreateDTO.getAcquisitionDate());

        return productStock;
    }

    private ProductStockDTO convertStockToDTO(ProductStock productStock) {
        ProductStockDTO productStockDTO = new ProductStockDTO();
        productStockDTO.setIdStock(productStock.getIdStock());
        productStockDTO.setIdProduct(productStock.getProduct().getIdProduct());
        productStockDTO.setQuantity(productStock.getQuantity());
        productStockDTO.setBatchNumber(productStock.getBatchNumber());
        productStockDTO.setExpirationDate(productStock.getExpirationDate());
        productStockDTO.setAcquisitionDate(productStock.getAcquisitionDate());

        return productStockDTO;
    }
}
