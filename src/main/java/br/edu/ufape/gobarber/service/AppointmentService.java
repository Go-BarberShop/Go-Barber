package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.appointment.AppointmentCreateDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentDTO;
import br.edu.ufape.gobarber.dto.page.PageAppointmentDTO;
import br.edu.ufape.gobarber.exceptions.AppointmentException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberService barberService;
    private final ServicesService servicesService;
    private final UserService userService;

    public PageAppointmentDTO getHistoryFromToken(Integer page, Integer size, HttpServletRequest request) throws DataBaseException {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findById(userService.getJtiFromToken(token));

        if (user.isPresent()){
            Barber barber = barberService.getBarberEntity(user.get());

            return getHistoryByBarber(page, size, barber.getIdBarber());

        }
        throw new DataBaseException("Não existe perfil de barbeiro associado a esse login");
    }

    public PageAppointmentDTO getFutureAppointments(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> pageApp =  appointmentRepository.findByStartTimeAfterOrderByStartTime(pageable, LocalDateTime.now());
        Page<AppointmentDTO> appointmentDTOS = pageApp.map(this::convertEntityToDTO);

        return new PageAppointmentDTO(
                appointmentDTOS.getTotalElements(),
                appointmentDTOS.getTotalPages(),
                appointmentDTOS.getPageable().getPageNumber(),
                appointmentDTOS.getSize(),
                appointmentDTOS.getContent()
        );
    }

    public PageAppointmentDTO getFutureAppointments(Integer page, Integer size, HttpServletRequest request) throws DataBaseException {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findById(userService.getJtiFromToken(token));
        Barber barber = null;
        if (user.isPresent()){
           barber = barberService.getBarberEntity(user.get());
        }

        return getPageAppointmentDTO(page, size, barber);
    }

    public PageAppointmentDTO getFutureAppointments(Integer page, Integer size, Integer barberId) throws DataBaseException {
        Barber barber = barberService.getBarberEntity(barberId);

        return getPageAppointmentDTO(page, size, barber);
    }

    private PageAppointmentDTO getPageAppointmentDTO(Integer page, Integer size, Barber barber) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> pageApp =  appointmentRepository.findByBarberAndStartTimeAfterOrderByStartTime(pageable, barber, LocalDateTime.now());
        Page<AppointmentDTO> appointmentDTOS = pageApp.map(this::convertEntityToDTO);

        return new PageAppointmentDTO(
                appointmentDTOS.getTotalElements(),
                appointmentDTOS.getTotalPages(),
                appointmentDTOS.getPageable().getPageNumber(),
                appointmentDTOS.getSize(),
                appointmentDTOS.getContent()
        );
    }

    public PageAppointmentDTO getHistoryByBarber(Integer page, Integer size, Integer barberId) throws DataBaseException {
        Barber barber = barberService.getBarberEntity(barberId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> pageApp =  appointmentRepository.findByBarberAndEndTimeBeforeOrderByStartTime(pageable,  barber, LocalDateTime.now());
        Page<AppointmentDTO> appointmentDTOS = pageApp.map(this::convertEntityToDTO);

        return new PageAppointmentDTO(
                appointmentDTOS.getTotalElements(),
                appointmentDTOS.getTotalPages(),
                appointmentDTOS.getPageable().getPageNumber(),
                appointmentDTOS.getSize(),
                appointmentDTOS.getContent()
        );
    }

    // Salvar novo agendamento
    public AppointmentDTO saveAppointment(AppointmentCreateDTO appointmentCreateDTO) throws AppointmentException, DataBaseException {
        // Verifica se existe um agendamento no mesmo espaço de horário

       Appointment appointment = convertDTOtoEntity(appointmentCreateDTO);

       try {
           if (isValidAppointment(appointment)) {
               return convertEntityToDTO(appointmentRepository.save(appointment));
           }
       } catch (AppointmentException e){
           throw new AppointmentException(e.getMessage());
       }

       return null;
    }

    // Atualizar agendamento existente
    @Transactional
    public AppointmentDTO updateAppointment(Integer id, AppointmentCreateDTO appointmentCreateDTO) throws DataBaseException, AppointmentException {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new DataBaseException("Não existe agentamento com o id informado"));

        Appointment appointmentUpdate = convertDTOtoEntity(appointmentCreateDTO);

        appointment.setClientName(appointmentUpdate.getClientName());
        appointment.setClientNumber(appointmentUpdate.getClientNumber());
        appointment.setBarber(appointmentUpdate.getBarber());
        appointment.setServiceType(appointmentUpdate.getServiceType());
        appointment.setStartTime(appointmentUpdate.getStartTime());
        appointment.setEndTime(appointmentUpdate.getEndTime());
        appointment.setTotalPrice(appointmentUpdate.getTotalPrice());

        try {
            if (isValidAppointment(appointment)) {
                return convertEntityToDTO(appointmentRepository.save(appointment));
            }
        } catch (AppointmentException e){
            throw new AppointmentException(e.getMessage());
        }

        return null;
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

    // Verificar se o agendamento é válido
    private boolean isValidAppointment(Appointment appointment) throws AppointmentException {
        if (appointment != null){

            if(appointment.getBarber() == null){
                throw new AppointmentException("É necessário selecionar um barbeiro para o agendamento");
            }

            Set<Services> servicesAppointment = appointment.getServiceType();
            Set<Services> servicesBarber = appointment.getBarber().getServices();

            for(Services s : servicesAppointment){
                if(!servicesBarber.contains(s)){
                    throw new AppointmentException("O barbeiro selecionado não está apto para algum desses serviços");
                }
            }

            if(appointment.getClientName().isBlank() || appointment.getClientNumber().isBlank()){
                throw new AppointmentException("As informações do cliente são campos obrigatórios");
            }

            isTimeValidated(appointment);

            return true;
        }
        throw new AppointmentException("O agendamento não pode ser nulo");
    }

    private void isTimeValidated(Appointment appointment){
        if(appointment.getStartTime().isBefore(LocalDateTime.now())){
            throw new AppointmentException("Não é possivel agendar para um dia/horário passado");
        }

        if(!isTimeSlotOccupied(appointment.getBarber(), appointment.getStartTime(), appointment.getEndTime()) &&
                isTimeSlotOccupied(appointment.getBarber(), appointment.getStartTime(), appointment.getEndTime(), appointment.getId())){
            throw new AppointmentException("O barbeiro selecionado já está reservado nesse horário");
        }

        if((appointment.getStartTime().toLocalTime().isBefore(appointment.getBarber().getStart())) ||
                (appointment.getStartTime().toLocalTime()).isAfter(appointment.getBarber().getEnd()) ||
                (appointment.getEndTime().toLocalTime().isAfter(appointment.getBarber().getEnd()))){

            throw new AppointmentException("O barbeiro selecionado não trabalha no horário agendado");
        }
    }

    // Verificar se o horário está ocupado por outro agendamento
    private boolean isTimeSlotOccupied(Barber barber, LocalDateTime start, LocalDateTime end) {
        List<Appointment> conflictingAppointments = appointmentRepository.findByBarberAndStartTimeBetween(barber, start, end);
        return conflictingAppointments.isEmpty();
    }

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
        Double price = 0.0;
        for(Integer id : appointmentCreateDTO.getServiceTypeIds()) {
            Services s = servicesService.getServiceEntity(id);
            services.add(s);
            timeMinutes += (s.getTimeService().getHour() + s.getTimeService().getMinute());
            price += s.getValueService();
        }

        appointment.setTotalPrice(price);

        appointment.setServiceType(services);

        LocalDateTime time = appointmentCreateDTO.getStartTime();

        appointment.setStartTime(time);

        time = time.plusMinutes(timeMinutes);

        appointment.setEndTime(time);

        return appointment;
    }

    private AppointmentDTO convertEntityToDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setClientName(appointment.getClientName());
        appointmentDTO.setClientNumber(appointment.getClientNumber());
        appointmentDTO.setBarber(barberService.convertToCompleteDTO(appointment.getBarber()));
        appointmentDTO.setServiceType(appointment.getServiceType());
        appointmentDTO.setTotalPrice(appointment.getTotalPrice());

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
}