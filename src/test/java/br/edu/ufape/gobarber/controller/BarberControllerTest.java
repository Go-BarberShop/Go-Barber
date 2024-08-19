package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.dto.barber.BarberServiceDTO;
import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.dto.page.PageBarberDTO;
import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.service.BarberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberController.class)
@AutoConfigureMockMvc(addFilters = false)
class BarberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarberService barberService;

    @Autowired
    private ObjectMapper objectMapper;

    private BarberCreateDTO barberCreateDTO;
    private BarberWithServiceDTO barberWithServiceDTO;

    @BeforeEach
    public void setUp() {
        barberCreateDTO = new BarberCreateDTO();
        barberCreateDTO.setName("John Doe");
        barberCreateDTO.setCpf("12345678901");
        barberCreateDTO.setAddressId(1);
        barberCreateDTO.setSalary(3000.00);
        barberCreateDTO.setAdmissionDate(LocalDate.now());
        barberCreateDTO.setWorkload(40);

        barberWithServiceDTO = new BarberWithServiceDTO();
        barberWithServiceDTO.setName("John Doe");
        barberWithServiceDTO.setCpf("12345678901");
        barberWithServiceDTO.setAddress(1);
        barberWithServiceDTO.setSalary(3000.00);
        barberWithServiceDTO.setAdmissionDate(LocalDate.now());
        barberWithServiceDTO.setWorkload(40);
    }

    @Test
    void deveCriarBarbeiroComSucesso() throws Exception {
        MockMultipartFile barberFile = new MockMultipartFile("barber", "", "application/json", objectMapper.writeValueAsBytes(barberCreateDTO));
        MockMultipartFile profilePhoto = new MockMultipartFile("profilePhoto", "photo.jpg", "image/jpeg", new byte[0]);

        when(barberService.createBarber(any(BarberCreateDTO.class), any())).thenReturn(barberWithServiceDTO);

        mockMvc.perform(multipart("/barber")
                        .file(barberFile)
                        .file(profilePhoto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(barberWithServiceDTO.getName()));
    }

    @Test
    void deveRetornarBadRequestQuandoCriarBarbeiroComDadosInvalidos() throws Exception {
        // Criação de um objeto BarberCreateDTO com dados inválidos (por exemplo, CPF inválido)
        barberCreateDTO.setCpf("invalid-cpf");

        MockMultipartFile barberFile = new MockMultipartFile("barber", "", "application/json", objectMapper.writeValueAsBytes(barberCreateDTO));
        MockMultipartFile profilePhoto = new MockMultipartFile("profilePhoto", "photo.jpg", "image/jpeg", new byte[0]);

        // Configuração do mock para retornar uma exceção de validação
        when(barberService.createBarber(any(BarberCreateDTO.class), any())).thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(multipart("/barber")
                        .file(barberFile)
                        .file(profilePhoto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deveDeletarBarbeiroComSucesso() throws Exception {
        doNothing().when(barberService).deleteBarber(anyInt());

        mockMvc.perform(delete("/barber/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deveObterBarbeiroPorIdComSucesso() throws Exception {
        when(barberService.getBarber(anyInt())).thenReturn(barberWithServiceDTO);

        mockMvc.perform(get("/barber/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(barberWithServiceDTO.getName()));
    }

    @Test
    void deveObterTodosOsBarbeirosComSucesso() throws Exception {
        PageBarberDTO pageBarberDTO = new PageBarberDTO(1L, 1, 0, 10, List.of(barberWithServiceDTO));

        when(barberService.getAllBarbers(anyInt(), anyInt())).thenReturn(pageBarberDTO);

        mockMvc.perform(get("/barber")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(pageBarberDTO.getTotalElements()));
    }

    @Test
    void deveObterFotoDePerfilComSucesso() throws Exception {
        byte[] photo = new byte[0]; // Simule uma foto de perfil

        when(barberService.getProfilePhoto(anyInt())).thenReturn(photo);

        mockMvc.perform(get("/barber/{id}/profile-photo", 1))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"Barbeiro-photo.jpeg\""));
    }

    @Test
    void deveRetornar404QuandoFotoDePerfilNaoExiste() throws Exception {
        when(barberService.getProfilePhoto(anyInt())).thenReturn(null);

        mockMvc.perform(get("/barber/{id}/profile-photo", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAdicionarServicoAoBarbeiroComSucesso() throws Exception {
        // Criação de um objeto ServicesDTO
        ServicesDTO serviceDTO = new ServicesDTO();
        serviceDTO.setId(1);
        serviceDTO.setName("Haircut"); // Nome do serviço

        // Criação do BarberServiceDTO
        BarberServiceDTO barberServiceDTO = new BarberServiceDTO();
        barberServiceDTO.setIdBarber(1);
        barberServiceDTO.setIdServices(Collections.singletonList(1));

        // Criação do BarberWithServiceDTO com um Set de ServicesDTO
        BarberWithServiceDTO barberWithServiceDTO = new BarberWithServiceDTO();
        barberWithServiceDTO.setName("John Doe");
        barberWithServiceDTO.setServices(Set.of(serviceDTO)); // Usando um Set<ServicesDTO>

        // Mock para o serviço
        when(barberService.addServiceToBarber(any(BarberServiceDTO.class))).thenReturn(barberWithServiceDTO);

        // Execução do teste
        mockMvc.perform(post("/barber/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(barberServiceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(barberWithServiceDTO.getName()))
                .andExpect(jsonPath("$.services[0].name").value(serviceDTO.getName()));
    }

    @Test
    void deveRemoverServicoDoBarbeiroComSucesso() throws Exception {
        // Criação de um objeto ServicesDTO
        ServicesDTO serviceDTO = new ServicesDTO();
        serviceDTO.setId(1);
        serviceDTO.setName("Haircut"); // Nome do serviço

        // Criação do BarberWithServiceDTO com um Set de ServicesDTO vazio
        BarberWithServiceDTO barberWithServiceDTO = new BarberWithServiceDTO();
        barberWithServiceDTO.setName("John Doe");
        barberWithServiceDTO.setServices(Set.of()); // Lista de serviços vazia após remoção

        // Mock para o serviço
        when(barberService.removeServiceFromBarber(anyInt(), anyInt())).thenReturn(barberWithServiceDTO);

        // Execução do teste
        mockMvc.perform(post("/barber/service/remove")
                        .param("barber", "1")
                        .param("service", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(barberWithServiceDTO.getName()))
                .andExpect(jsonPath("$.services").isEmpty());
    }

    @Test
    void deveObterFotoPerfilDoBarbeiroComSucesso() throws Exception {
        byte[] photo = new byte[]{1, 2, 3, 4, 5}; // Exemplo de conteúdo de foto

        when(barberService.getProfilePhoto(anyInt())).thenReturn(photo);

        mockMvc.perform(get("/barber/{id}/profile-photo", 1))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Barbeiro-photo.jpeg\""))
                .andExpect(content().bytes(photo));
    }

}