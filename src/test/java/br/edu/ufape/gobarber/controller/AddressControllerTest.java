package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.controller.AddressController;
import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.service.AddressService;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address();
        address.setIdAddress(Integer.valueOf("1"));
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setCep("62704");
    }

    @Test
    void deveCriarEnderecoComSucesso() throws Exception {
        // Certifique-se de definir todos os campos obrigatórios
        address.setStreet("Main St");
        address.setCity("Nashville");
        address.setState("TN");
        address.setCep("62704-000");
        address.setNeighborhood("Downtown"); // Adicione todos os campos obrigatórios

        when(addressService.creatAddress(any(AddressCreateDTO.class))).thenReturn(address);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAddress").value(address.getIdAddress()))
                .andExpect(jsonPath("$.street").value(address.getStreet()));
    }

    @Test
    void deveAtualizarEnderecoComSucesso() throws Exception {
        when(addressService.updateAddress(anyInt(), any(Address.class))).thenReturn(address);

        mockMvc.perform(put("/address/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAddress").value(address.getIdAddress()))
                .andExpect(jsonPath("$.street").value(address.getStreet()));
    }

    @Test
    void deveDeletarEnderecoComSucesso() throws Exception {
        doNothing().when(addressService).deleteAddress(anyInt());

        mockMvc.perform(delete("/address/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deveObterEnderecoPorIdComSucesso() throws Exception {
        when(addressService.getAddressById(anyInt())).thenReturn(address);

        mockMvc.perform(get("/address/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAddress").value(address.getIdAddress()))
                .andExpect(jsonPath("$.street").value(address.getStreet()));
    }

    @Test
    void deveObterTodosEnderecosComSucesso() throws Exception {
        when(addressService.getAllAddresses()).thenReturn(List.of(address));

        mockMvc.perform(get("/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAddress").value(address.getIdAddress()))
                .andExpect(jsonPath("$[0].street").value(address.getStreet()));
    }

    @Test
    void deveRetornar500QuandoEnderecoNaoExiste() throws Exception {
        when(addressService.getAddressById(anyInt())).thenThrow(new DataBaseException("Endereço não encontrado!"));

        mockMvc.perform(get("/address/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarErroAoCriarEnderecoComCepInvalido() throws Exception {
        Address addressWithInvalidZip = new Address();
        addressWithInvalidZip.setStreet("Rua dos bobos");
        addressWithInvalidZip.setCity("Garanhuns");
        addressWithInvalidZip.setState("PE");
        addressWithInvalidZip.setCep("INVALID_CEP");

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressWithInvalidZip)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaRequisicaoMalformada() throws Exception {
        // JSON malformado (por exemplo, faltando uma chave obrigatória)
        String invalidJson = "{\"street\":\"123 Pine St\",\"city\":\"Smallville\",\"state\":\"KS\"}"; // Faltando 'cep'

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists()); // Verifica que um erro de validação foi retornado
    }

    @Test
    void deveRetornar400ParaEnderecoComCamposNulos() throws Exception {
        Address addressWithNullFields = new Address();
        addressWithNullFields.setStreet(null);  // Campo obrigatório, mas estamos testando como o sistema lida com nulos
        addressWithNullFields.setCity("Nashville");
        addressWithNullFields.setState("TN");
        addressWithNullFields.setCep("37201");

        when(addressService.creatAddress(any(AddressCreateDTO.class))).thenReturn(addressWithNullFields);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressWithNullFields)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists()); // Verifica que um erro de validação foi retornado
    }

    @Test
    void deveAtualizarEnderecoSemAlteracoes() throws Exception {
        // Endereço a ser retornado como resultado da atualização, sem alterações
        Address updatedAddress = new Address();
        updatedAddress.setIdAddress(1);
        updatedAddress.setStreet("123 Main St");
        updatedAddress.setCity("Springfield");
        updatedAddress.setState("IL");
        updatedAddress.setCep("62704");

        when(addressService.updateAddress(anyInt(), any(Address.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/address/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAddress").value(updatedAddress.getIdAddress()))
                .andExpect(jsonPath("$.street").value(updatedAddress.getStreet()))
                .andExpect(jsonPath("$.city").value(updatedAddress.getCity()))
                .andExpect(jsonPath("$.state").value(updatedAddress.getState()))
                .andExpect(jsonPath("$.cep").value(updatedAddress.getCep()));
    }

}

