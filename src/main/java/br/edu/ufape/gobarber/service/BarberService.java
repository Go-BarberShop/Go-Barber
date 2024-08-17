package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.repository.AddressRepository;
import br.edu.ufape.gobarber.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Barber createBarber(@Valid BarberCreateDTO barberCreateDTO, MultipartFile profilePhoto) throws DataBaseException {
        try {

            Barber barber = convertDTOtoEntity(barberCreateDTO);

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                barber.setProfilePhoto(profilePhoto.getBytes());
            }

            return barberRepository.save(barber);
        } catch (IOException e) {
            throw new DataBaseException("Erro ao processar a foto de perfil.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Barber updateBarber(Integer id, Barber updatedBarber, MultipartFile profilePhoto) throws DataBaseException {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado no banco de dados"));

        barber.setName(updatedBarber.getName());
        barber.setCpf(updatedBarber.getCpf());
        barber.setAddress(updatedBarber.getAddress());
        barber.setSalary(updatedBarber.getSalary());
        barber.setAdmissionDate(updatedBarber.getAdmissionDate());
        barber.setWorkload(updatedBarber.getWorkload());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                barber.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new DataBaseException("Erro ao processar a foto de perfil.");
            }
        }
        return barberRepository.save(barber);
    }

    @Transactional
    public void deleteBarber(Integer id) {
        Optional<Barber> barber = barberRepository.findById(id);
        barber.ifPresent(barberRepository::delete);
    }

    public Barber getBarber(Integer id) throws DataBaseException {
        return barberRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado!"));
    }

    public Page<Barber> getAllBarbers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return barberRepository.findAll(pageable);
    }

    public byte[] getProfilePhoto(Integer id) throws DataBaseException {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado!"));

        return barber.getProfilePhoto();
    }

    private Barber convertDTOtoEntity(BarberCreateDTO barberCreateDTO) {
        Barber barber = new Barber();
        barber.setName(barberCreateDTO.getName());
        barber.setCpf(barberCreateDTO.getCpf());

        // Buscar o endereço pelo ID fornecido
        Address address = addressRepository.findById(barberCreateDTO.getAddressId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        barber.setAddress(address);
        barber.setSalary(barberCreateDTO.getSalary());
        barber.setAdmissionDate(barberCreateDTO.getAdmissionDate());
        barber.setWorkload(barberCreateDTO.getWorkload());
        return barber;
    }
}
