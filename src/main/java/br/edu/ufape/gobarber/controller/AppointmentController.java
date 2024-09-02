package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.appointment.AppointmentCreateDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentDTO;
import br.edu.ufape.gobarber.dto.page.PageAppointmentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

import br.edu.ufape.gobarber.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@Validated
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Criar novo agendamento
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreateDTO appointment) throws DataBaseException {
        AppointmentDTO savedAppointment = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    // Atualizar agendamento existente
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id, @RequestBody AppointmentCreateDTO appointment) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    // Obter agendamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) throws DataBaseException {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    // Obter todos os agendamentos
    @GetMapping
    public ResponseEntity<PageAppointmentDTO> getAllAppointments(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz) {
        PageAppointmentDTO appointments = appointmentService.getAllAppointments(page, siz);
        return ResponseEntity.ok(appointments);
    }

    // Obter agendamentos por barbeiro
    @GetMapping("/barber/{barberId}")
    public ResponseEntity<PageAppointmentDTO> getAppointmentsByBarber(@PathVariable Integer barberId,
                                                                      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz) throws DataBaseException {
        PageAppointmentDTO appointments = appointmentService.getAppointmentsByBarber(barberId, page, siz);
        return ResponseEntity.ok(appointments);
    }

    // Excluir agendamento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }
}
