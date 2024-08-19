package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.page.PageSaleDTO;
import br.edu.ufape.gobarber.dto.sale.SaleCreateDTO;
import br.edu.ufape.gobarber.dto.sale.SaleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @Autowired
    private ObjectMapper objectMapper;

    private SaleDTO saleDTO;
    private SaleCreateDTO saleCreateDTO;

    @BeforeEach
    public void setUp() {
        saleCreateDTO = new SaleCreateDTO();
        saleCreateDTO.setName("Summer Sale");
        saleCreateDTO.setTotalPrice(100.00);
        saleCreateDTO.setStartDate(LocalDate.now());
        saleCreateDTO.setEndDate(LocalDate.now().plusDays(10));
        saleCreateDTO.setCoupon("SUMM123");

        saleDTO = new SaleDTO();
        saleDTO.setId(1);
        saleDTO.setName("Summer Sale");
        saleDTO.setTotalPrice(100.00);
        saleDTO.setStartDate(LocalDate.now());
        saleDTO.setEndDate(LocalDate.now().plusDays(10));
        saleDTO.setCoupon("SUMM123");
    }

    @Test
    void deveCriarVendaComSucesso() throws Exception {
        when(saleService.createSale(any(SaleCreateDTO.class))).thenReturn(saleDTO);

        mockMvc.perform(post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.name").value(saleDTO.getName()));
    }

    @Test
    void deveAtualizarVendaComSucesso() throws Exception {
        when(saleService.updateSale(anyInt(), any(SaleCreateDTO.class))).thenReturn(saleDTO);

        mockMvc.perform(put("/sale/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.name").value(saleDTO.getName()));
    }

    @Test
    void deveDeletarVendaComSucesso() throws Exception {
        doNothing().when(saleService).delete(anyInt());

        mockMvc.perform(delete("/sale/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deveObterVendaPorIdComSucesso() throws Exception {
        when(saleService.getSale(anyInt())).thenReturn(saleDTO);

        mockMvc.perform(get("/sale/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.name").value(saleDTO.getName()));
    }

    @Test
    void deveObterTodasAsVendasComSucesso() throws Exception {
        PageSaleDTO pageSaleDTO = new PageSaleDTO(1L, 1, 0, 10, List.of(saleDTO));

        when(saleService.getAllSales(anyInt(), anyInt())).thenReturn(pageSaleDTO);

        mockMvc.perform(get("/sale")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(pageSaleDTO.getTotalElements()));
    }

    @Test
    void deveObterTodasVendasValidasComSucesso() throws Exception {
        PageSaleDTO pageSaleDTO = new PageSaleDTO(1L, 1, 0, 10, List.of(saleDTO));

        when(saleService.getAllValidSales(anyInt(), anyInt())).thenReturn(pageSaleDTO);

        mockMvc.perform(get("/sale/valid")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(pageSaleDTO.getTotalElements()));
    }

    @Test
    void deveObterVendaPorCupomComSucesso() throws Exception {
        when(saleService.getSaleByCoupon(any())).thenReturn(saleDTO);

        mockMvc.perform(get("/sale/coupon/{coupon}", "SUMM123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.name").value(saleDTO.getName()));
    }

    @Test
    void deveEnviarEmailPromocionalComSucesso() throws Exception {
        doNothing().when(saleService).sendPromotionalEmail(anyInt());

        mockMvc.perform(post("/sale/email/notify")
                        .param("idSale", "1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void deveRetornar500QuandoVendaNaoExiste() throws Exception {
        when(saleService.getSale(anyInt())).thenThrow(new DataBaseException("Promoção não encontrada!"));

        mockMvc.perform(get("/sale/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarErroAoCriarVendaComDataFinalNoPassado() throws Exception {
        SaleCreateDTO saleCreateDTOWithPastEndDate = new SaleCreateDTO();
        saleCreateDTOWithPastEndDate.setName("Spring Sale");
        saleCreateDTOWithPastEndDate.setTotalPrice(150.00);
        saleCreateDTOWithPastEndDate.setStartDate(LocalDate.now().minusDays(5));
        saleCreateDTOWithPastEndDate.setEndDate(LocalDate.now().minusDays(1)); // Data final no passado
        saleCreateDTOWithPastEndDate.setCoupon("SPRNG20");

        mockMvc.perform(post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleCreateDTOWithPastEndDate)))
                .andExpect(status().isBadRequest());
    }

}
