package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.dto.barber.BarberServiceDTO;
import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.dto.page.PageBarberDTO;
import br.edu.ufape.gobarber.dto.page.PageSecretaryDTO;
import br.edu.ufape.gobarber.dto.secretary.SecretaryCreateDTO;
import br.edu.ufape.gobarber.dto.secretary.SecretaryDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.JsonParsingException;
import br.edu.ufape.gobarber.service.BarberService;
import br.edu.ufape.gobarber.service.SecretaryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/secretary")
@Validated
@Slf4j
public class SecretaryController {

    private final SecretaryService secretaryService;

    public SecretaryController(SecretaryService secretaryService) {
        this.secretaryService = secretaryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SecretaryDTO> createSecretary(@Valid @RequestPart(value = "secretary") String secretaryJson,
                                                     @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        SecretaryCreateDTO secretary;
        objectMapper.registerModule(new JavaTimeModule());
        try {
            secretary = objectMapper.readValue(secretaryJson, SecretaryCreateDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
        SecretaryDTO newSecretary = secretaryService.createSecretary(secretary, profilePhoto);
        return new ResponseEntity<>(newSecretary, HttpStatus.CREATED);
    }

    @PostMapping("/create-without-photo")
    public ResponseEntity<SecretaryDTO> createSecretaryWithoutPicture(@RequestBody SecretaryCreateDTO secretaryCreateDTO) throws DataBaseException {
        SecretaryDTO secretary = secretaryService.createSecretary(secretaryCreateDTO, null);
        return new ResponseEntity<>(secretary, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SecretaryDTO> updateSecretary(@PathVariable Integer id,
                                               @RequestPart(value = "secretary") String secretaryJson,
                                               @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        SecretaryCreateDTO updatedSecretarry;

        // Registrar o módulo para datas Java 8
        objectMapper.registerModule(new JavaTimeModule());
        try {
            updatedSecretarry = objectMapper.readValue(secretaryJson, SecretaryCreateDTO.class);
        } catch (Exception e) {
            throw new JsonParsingException("Error parsing JSON", e);
        }

        SecretaryDTO updated = secretaryService.updateSecretary(id, updatedSecretarry, profilePhoto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecretary(@PathVariable Integer id) {
        secretaryService.deleteSecretary(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretaryDTO> getSecretary(@PathVariable Integer id) throws DataBaseException {
        SecretaryDTO secretary = secretaryService.getSecretary(id);
        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    @GetMapping("/logged-secretary")
    public ResponseEntity<SecretaryDTO> getLoggedSecretaryInfo(HttpServletRequest request) throws DataBaseException {
        SecretaryDTO secretary = secretaryService.getSecretary(request);

        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    @GetMapping("/logged-secretary/picture")
    public ResponseEntity<byte[]> getLoggedSecretaryProfilePhoto(HttpServletRequest request) throws DataBaseException {
        return getProfilePhoto(secretaryService.getProfilePhoto(request));
    }

    @GetMapping("/{id}/profile-photo")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer id) throws DataBaseException {
        return getProfilePhoto(secretaryService.getProfilePhoto(id));
    }

    @GetMapping
    public ResponseEntity<PageSecretaryDTO> getAllBarbers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        PageSecretaryDTO secretary = secretaryService.getAllSecretaries(page, size);
        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> getProfilePhoto(byte[] profilePhoto) throws DataBaseException {
        byte[] image = profilePhoto;

        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Secretaria-photo.jpeg").build());

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
