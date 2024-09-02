package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.appointment.AppointmentCreateDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentDTO;
import br.edu.ufape.gobarber.dto.page.PageAppointmentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import br.edu.ufape.gobarber.repository.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberService barberService;
    private final ServicesService servicesService;

    public AppointmentService(AppointmentRepository appointmentRepository, BarberService barberService, ServicesService servicesService) {
        this.appointmentRepository = appointmentRepository;
        this.barberService = barberService;
        this.servicesService = servicesService;
    }

    // Salvar novo agendamento
    public AppointmentDTO saveAppointment(AppointmentCreateDTO appointmentCreateDTO) throws DataBaseException {
        // Verifica se existe um agendamento no mesmo espaço de horário

       Appointment appointment = convertDTOtoEntity(appointmentCreateDTO);

        if (isTimeSlotOccupied(appointment.getBarber(), appointment.getStartTime(), appointment.getEndTime())) {
            throw new IllegalArgumentException("Horário de agendamento já ocupado para este barbeiro.");
        }

        return convertEntityToDTO(appointmentRepository.save(appointment));
    }

    // Atualizar agendamento existente
    @Transactional
    public AppointmentDTO updateAppointment(Integer id, AppointmentCreateDTO appointmentCreateDTO) throws DataBaseException {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new DataBaseException("Não existe agentamento com o id informado"));

        Appointment appointmentUpdate = convertDTOtoEntity(appointmentCreateDTO);

        appointment.setClientName(appointmentUpdate.getClientName());
        appointment.setClientNumber(appointmentUpdate.getClientNumber());
        appointment.setBarber(appointmentUpdate.getBarber());
        appointment.setServiceType(appointmentUpdate.getServiceType());
        appointment.setStartTime(appointmentUpdate.getStartTime());
        appointment.setEndTime(appointmentUpdate.getEndTime());

        //Validar

        return convertEntityToDTO(appointmentRepository.save(appointment));
    }

    // Verificar se o horário está ocupado por outro agendamento
    private boolean isTimeSlotOccupied(Barber barber, LocalDateTime start, LocalDateTime end) {
        List<Appointment> conflictingAppointments = appointmentRepository.findByBarberAndStartTimeBetween(barber, start, end);
        return !conflictingAppointments.isEmpty();
    }

    // Verificar se o horário está ocupado por outro agendamento (excluindo o atual)
    private boolean isTimeSlotOccupied(Barber barber, LocalDateTime start, LocalDateTime end, Integer appointmentId) {
        List<Appointment> conflictingAppointments = appointmentRepository.findByBarberAndStartTimeBetween(barber, start, end);
        return conflictingAppointments.stream().anyMatch(a -> !a.getId().equals(appointmentId));
    }

    public Appointment convertDTOtoEntity(AppointmentCreateDTO appointmentCreateDTO) throws DataBaseException {
        Appointment appointment = new Appointment();
        appointment.setClientName(appointmentCreateDTO.getClientName());
        appointment.setClientNumber(appointmentCreateDTO.getClientNumber());
        appointment.setBarber(barberService.getBarberEntity(appointmentCreateDTO.getBarberId()));

        Set<Services> services = new HashSet<>();
        Integer timeMinutes = 0;
        for(Integer id : appointmentCreateDTO.getServiceTypeIds()) {
            Services s = servicesService.getServiceEntity(id);
            services.add(s);
            timeMinutes += (s.getTimeService().getHour() + s.getTimeService().getMinute());
        }

        appointment.setServiceType(services);

        LocalDateTime time = LocalDateTime.parse(appointmentCreateDTO.getStartTime(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        appointment.setStartTime(time);

        time = time.plusMinutes(timeMinutes);

        appointment.setEndTime(time);

        return appointment;
    }

    private AppointmentDTO convertEntityToDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setClientName(appointment.getClientName());
        appointmentDTO.setClientNumber(appointment.getClientNumber());
        appointmentDTO.setBarber(barberService.convertToCompleteDTO(appointment.getBarber()));
        appointmentDTO.setServiceType(appointment.getServiceType());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String timeString;

        if(appointment.getStartTime() != null) {
            timeString = appointment.getStartTime() .format(formatter);
            appointmentDTO.setStartTime(timeString);
        }

        if(appointment.getEndTime() != null) {
            timeString = appointment.getEndTime().format(formatter);
            appointmentDTO.setEndTime(timeString);
        }

        return appointmentDTO;
    }

    // Obter todos os agendamentos
    public PageAppointmentDTO getAllAppointments(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> pageApp = appointmentRepository.findAllByOrderByStartTimeAsc(pageable);
        Page<AppointmentDTO> appointmentDTOS = pageApp.map(this::convertEntityToDTO);

        return new PageAppointmentDTO(
                appointmentDTOS.getTotalElements(),
                appointmentDTOS.getTotalPages(),
                appointmentDTOS.getPageable().getPageNumber(),
                appointmentDTOS.getSize(),
                appointmentDTOS.getContent()
        );
    }

    // Obter agendamentos por barbeiro
    public PageAppointmentDTO getAppointmentsByBarber(Integer barberid, Integer page, Integer size) throws DataBaseException {
        Barber barber = barberService.getBarberEntity(barberid);

        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> pageApp =  appointmentRepository.findByBarber(barber, pageable);
        Page<AppointmentDTO> appointmentDTOS = pageApp.map(this::convertEntityToDTO);

        return new PageAppointmentDTO(
                appointmentDTOS.getTotalElements(),
                appointmentDTOS.getTotalPages(),
                appointmentDTOS.getPageable().getPageNumber(),
                appointmentDTOS.getSize(),
                appointmentDTOS.getContent()
        );
    }

    // Obter agendamento por ID
    public AppointmentDTO getAppointmentById(Integer id) throws DataBaseException {
        Appointment appointment =  appointmentRepository.findById(id).orElseThrow(() -> new DataBaseException("Não existe agendamento com esse ID"));

        return convertEntityToDTO(appointment);
    }

    // Excluir agendamento por ID
    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }
}