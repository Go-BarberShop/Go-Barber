package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.services.ServicesCreateDTO;
import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Services;
import br.edu.ufape.gobarber.service.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services")
@Validated
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping
    public ResponseEntity<ServicesDTO> createService(@Valid @RequestBody ServicesCreateDTO servicesCreateDTO) {
        ServicesDTO servicesDTO = servicesService.createServices(servicesCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicesDTO> updateServices(@Valid @RequestBody ServicesCreateDTO servicesCreateDTO, @PathVariable Integer id) throws DataBaseException {

        ServicesDTO servicesDTO = servicesService.updateServices(id, servicesCreateDTO);
        return new ResponseEntity<>(servicesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServices(@PathVariable Integer id) {

        servicesService.deleteServices(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesDTO> getServices(@PathVariable Integer id) throws DataBaseException {
        return new ResponseEntity<>(servicesService.getServices(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Services>> getAllServices(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        List<Services> services = servicesService.getAllServices(page, size).getContent();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

}
