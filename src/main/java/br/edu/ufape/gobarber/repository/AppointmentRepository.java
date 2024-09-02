package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Barber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findAllByOrderByStartTimeAsc(Pageable pageable);
    List<Appointment> findByBarberAndStartTimeBetween(Barber barber, LocalDateTime start, LocalDateTime end);
    Page<Appointment> findByBarber(Barber barber, Pageable pageable);
}
