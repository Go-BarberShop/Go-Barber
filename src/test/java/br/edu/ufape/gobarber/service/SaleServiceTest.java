package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageSaleDTO;
import br.edu.ufape.gobarber.dto.sale.SaleCreateDTO;
import br.edu.ufape.gobarber.dto.sale.SaleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseConstraintException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Sale;
import br.edu.ufape.gobarber.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private EmailService emailService;

    private Sale sale;

    @BeforeEach
    void setUp() {
        sale = new Sale();
        sale.setIdSale(1);
        sale.setName("Promoção Teste");
        sale.setTotalPrice(100.0);
        sale.setStartDate(LocalDate.now());
        sale.setEndDate(LocalDate.now().plusDays(10));
        sale.setCoupon("ABC1234");
    }

    @Test
    void deveCriarUmaSaleComSucesso() throws DataBaseException, DataBaseConstraintException {
        // Arrange
        SaleCreateDTO saleCreateDTO = new SaleCreateDTO();
        saleCreateDTO.setName("Promoção Teste");
        saleCreateDTO.setTotalPrice(100.0);
        saleCreateDTO.setStartDate(LocalDate.now());
        saleCreateDTO.setEndDate(LocalDate.now().plusDays(10));
        saleCreateDTO.setCoupon("ABC1234");

        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // Act
        SaleDTO result = saleService.createSale(saleCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Promoção Teste", result.getName());
        assertEquals("ABC1234", result.getCoupon());
    }

    @Test
    void deveLancarExcecaoAoCriarSaleComCupomDuplicado() throws DataBaseException{
        // Arrange
        SaleCreateDTO saleCreateDTO = new SaleCreateDTO();
        saleCreateDTO.setName("Promoção Teste");
        saleCreateDTO.setTotalPrice(100.0);
        saleCreateDTO.setStartDate(LocalDate.now());
        saleCreateDTO.setEndDate(LocalDate.now().plusDays(10));
        saleCreateDTO.setCoupon("ABC1234");

        // Assert
        assertThrows(DataBaseException.class, () -> saleService.createSale(saleCreateDTO));
    }

    @Test
    void deveAtualizarUmaSaleComSucesso() throws DataBaseException {
        // Arrange
        SaleCreateDTO saleCreateDTO = new SaleCreateDTO();
        saleCreateDTO.setName("Promoção Atualizada");
        saleCreateDTO.setTotalPrice(200.0);
        saleCreateDTO.setStartDate(LocalDate.now());
        saleCreateDTO.setEndDate(LocalDate.now().plusDays(20));
        saleCreateDTO.setCoupon("XYZ5678");

        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // Act
        SaleDTO result = saleService.updateSale(1, saleCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Promoção Atualizada", result.getName());
        assertEquals("XYZ5678", result.getCoupon());
    }

    @Test
    void deveLancarExcecaoAoAtualizarSaleInexistente() {
        // Arrange
        SaleCreateDTO saleCreateDTO = new SaleCreateDTO();

        when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Assert
        assertThrows(DataBaseException.class, () -> saleService.updateSale(1, saleCreateDTO));
    }

    @Test
    void deveDeletarUmaSaleComSucesso() {
        // Arrange
        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(sale));

        // Act
        saleService.delete(1);

        // Assert
        verify(saleRepository, times(1)).delete(sale);
    }

    @Test
    void deveListarTodasAsSalesComSucesso() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> pageSale = new PageImpl<>(List.of(sale), pageable, 1);

        when(saleRepository.findAll(pageable)).thenReturn(pageSale);

        // Act
        PageSaleDTO result = saleService.getAllSales(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void deveBuscarUmaSalePorIdComSucesso() throws DataBaseException {
        // Arrange
        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(sale));

        // Act
        SaleDTO result = saleService.getSale(1);

        // Assert
        assertNotNull(result);
        assertEquals("Promoção Teste", result.getName());
    }

    @Test
    void deveLancarExcecaoAoBuscarSalePorIdInexistente() {
        // Arrange
        when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Assert
        assertThrows(DataBaseException.class, () -> saleService.getSale(1));
    }

    @Test
    void deveBuscarUmaSalePorCouponComSucesso() throws DataBaseException {
        // Arrange
        when(saleRepository.findByCoupon(anyString())).thenReturn(Optional.of(sale));

        // Act
        SaleDTO result = saleService.getSaleByCoupon("ABC1234");

        // Assert
        assertNotNull(result);
        assertEquals("Promoção Teste", result.getName());
        assertEquals("ABC1234", result.getCoupon());
    }

    @Test
    void deveLancarExcecaoAoBuscarSalePorCouponInexistente() {
        // Arrange
        when(saleRepository.findByCoupon(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(DataBaseException.class, () -> saleService.getSaleByCoupon("XYZ5678"));
    }
}