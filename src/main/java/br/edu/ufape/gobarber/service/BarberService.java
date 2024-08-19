package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.dto.barber.BarberServiceDTO;
import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.dto.page.PageBarberDTO;
import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private final AddressRepository addressRepository;
    private final ServicesService servicesService;

    @Transactional
    public BarberWithServiceDTO createBarber(@Valid BarberCreateDTO barberCreateDTO, MultipartFile profilePhoto) throws DataBaseException {
        try {

            Barber barber = convertDTOtoEntity(barberCreateDTO);

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                barber.setProfilePhoto(profilePhoto.getBytes());
            }

            return convertToCompleteDTO(barberRepository.save(barber));
        } catch (IOException e) {
            throw new DataBaseException("Erro ao processar a foto de perfil.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public BarberWithServiceDTO updateBarber(Integer id, Barber updatedBarber, MultipartFile profilePhoto) throws DataBaseException {
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
        return convertToCompleteDTO(barberRepository.save(barber));
    }

    @Transactional
    public void deleteBarber(Integer id) {
        Optional<Barber> barber = barberRepository.findById(id);
        barber.ifPresent(barberRepository::delete);
    }

    public BarberWithServiceDTO getBarber(Integer id) throws DataBaseException {
        return convertToCompleteDTO(barberRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado!")));
    }

    public PageBarberDTO getAllBarbers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Barber> barbers =  barberRepository.findAll(pageable);
        Page<BarberWithServiceDTO> barberPage = barbers.map(this::convertToCompleteDTO);

        return new PageBarberDTO(
                barberPage.getTotalElements(),
                barberPage.getTotalPages(),
                barberPage.getPageable().getPageNumber(),
                barberPage.getSize(),
                barberPage.getContent()
        );
    }

    public byte[] getProfilePhoto(Integer id) throws DataBaseException {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado!"));

        return barber.getProfilePhoto();
    }

    @Transactional
    public BarberWithServiceDTO addServiceToBarber(BarberServiceDTO barberServiceDTO) throws DataBaseException {

        Barber barber = barberRepository.findById(barberServiceDTO.getIdBarber()).orElseThrow(() -> new DataBaseException("Não existe barbeiro com esse id"));

        List<Services> servicesToAdd = new ArrayList<>();
        for(Integer id : barberServiceDTO.getIdServices()) {
            Services service = servicesService.getServiceEntity(id);
            servicesToAdd.add(service);
        }

        for(Services s : servicesToAdd){
            barber.addService(s);
        }

        barber = barberRepository.save(barber);

        return convertToCompleteDTO(barber);
    }

    @Transactional
    public BarberWithServiceDTO removeServiceFromBarber(Integer idBarber, Integer idService) throws DataBaseException {

        Barber barber = barberRepository.findById(idBarber).orElseThrow(() -> new DataBaseException("Não existe barbeiro com esse id"));

        Services service = servicesService.getServiceEntity(idService);

        barber.removeService(service);

        barber = barberRepository.save(barber);

        return convertToCompleteDTO(barber);
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

    private BarberWithServiceDTO convertToCompleteDTO(Barber barber){
        BarberWithServiceDTO dto = new BarberWithServiceDTO();

        dto.setIdBarber(barber.getIdBarber());
        dto.setName(barber.getName());
        dto.setCpf(barber.getCpf());

        // Converter Address para AddressDTO, supondo que você tenha um método para isso
        dto.setAddress(barber.getAddress().getIdAddress());

        dto.setSalary(barber.getSalary());
        dto.setAdmissionDate(barber.getAdmissionDate());
        dto.setWorkload(barber.getWorkload());

        // Converter o conjunto de Services para um conjunto de ServiceDTO
        Set<ServicesDTO> serviceDTOs = barber.getServices().stream()
                .map(servicesService::convertServicesToDTO)
                .collect(Collectors.toSet());
        dto.setServices(serviceDTOs);

        return dto;
    }

}
