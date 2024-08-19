package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.dto.barber.BarberServiceDTO;
import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.dto.page.PageBarberDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.service.BarberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/barber")
@Validated
@Slf4j
public class BarberController {

    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BarberWithServiceDTO> createBarber(@Valid @RequestPart(value = "barber") String barberJson,
                                               @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        BarberCreateDTO barber;
        // Registrar o módulo para datas Java 8
        objectMapper.registerModule(new JavaTimeModule());
        try {
            barber = objectMapper.readValue(barberJson, BarberCreateDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
        System.out.println(barber.toString());
        BarberWithServiceDTO newBarber = barberService.createBarber(barber, profilePhoto);
        return new ResponseEntity<>(newBarber, HttpStatus.CREATED);
    }

    @PostMapping("/service")
    public ResponseEntity<BarberWithServiceDTO> addServiceToBarber(@RequestBody BarberServiceDTO barberServiceDTO) throws DataBaseException {
        BarberWithServiceDTO dto = barberService.addServiceToBarber(barberServiceDTO);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/service/remove")
    public ResponseEntity<BarberWithServiceDTO> removeServiceBarber(@RequestParam(value = "barber", required = true) Integer idBarber,
                                                                    @RequestParam(value = "service", required = true) Integer idService) throws DataBaseException {
        BarberWithServiceDTO barber = barberService.removeServiceFromBarber(idBarber, idService);

        return new ResponseEntity<>(barber, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BarberWithServiceDTO> updateBarber(@PathVariable Integer id,
                                               @RequestPart(value = "barber") String barberJson,
                                               @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        Barber updatedBarber;

        // Registrar o módulo para datas Java 8
        objectMapper.registerModule(new JavaTimeModule());
        try {
            updatedBarber = objectMapper.readValue(barberJson, Barber.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }

        BarberWithServiceDTO updated = barberService.updateBarber(id, updatedBarber, profilePhoto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Integer id) {
        barberService.deleteBarber(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberWithServiceDTO> getBarber(@PathVariable Integer id) throws DataBaseException {
        BarberWithServiceDTO barber = barberService.getBarber(id);
        return new ResponseEntity<>(barber, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageBarberDTO> getAllBarbers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        PageBarberDTO barbers = barberService.getAllBarbers(page, size);
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }

    @GetMapping("/{id}/profile-photo")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer id) throws DataBaseException {
        byte[] image = barberService.getProfilePhoto(id);

        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Barbeiro-photo.jpeg").build());

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
