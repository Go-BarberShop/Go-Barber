package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.services.ServicesCreateDTO;
import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.ServiceAlreadyExistsException;
import br.edu.ufape.gobarber.model.Services;
import br.edu.ufape.gobarber.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServicesRepository servicesRepository;

    @Transactional
    public ServicesDTO createServices(@Valid ServicesCreateDTO servicesCreateDTO) {
        // Verifica se o serviço já existe pelo nome
        if (servicesRepository.existsByNameService(servicesCreateDTO.getName())) {
            throw new ServiceAlreadyExistsException("Serviço já cadastrado com o nome: " + servicesCreateDTO.getName());
        }

        Services services = new Services();
        services.setNameService(servicesCreateDTO.getName());
        services.setDescriptionService(servicesCreateDTO.getDescription());
        services.setValueService(servicesCreateDTO.getValue());
        services.setTimeService(servicesCreateDTO.getTime());

        services = servicesRepository.save(services);

        return convertServicesToDTO(services);
    }

    @Transactional
    public ServicesDTO updateServices(Integer id, ServicesCreateDTO servicesCreateDTO) throws DataBaseException {
        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Serviço não encontrado no banco de dados!"));
        services.setNameService(servicesCreateDTO.getName());
        services.setDescriptionService(servicesCreateDTO.getDescription());
        services.setValueService(servicesCreateDTO.getValue());
        services.setTimeService(servicesCreateDTO.getTime());

        services = servicesRepository.save(services);

        return convertServicesToDTO(services);
    }

    @Transactional
    public void deleteServices(Integer id) {
        Optional<Services> services = servicesRepository.findById(id);
        services.ifPresent(servicesRepository::delete);
    }

    public ServicesDTO getServices(Integer id) throws DataBaseException {
        Services services =  servicesRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Serviço não encontrado!"));

        return convertServicesToDTO(services);
    }

    public Page<Services> getAllServices(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return servicesRepository.findAll(pageable);
    }

    private ServicesDTO convertServicesToDTO(Services services) {
        ServicesDTO servicesDTO = new ServicesDTO();
        servicesDTO.setName(services.getNameService());
        servicesDTO.setDescription(services.getDescriptionService());
        servicesDTO.setValue(services.getValueService());
        servicesDTO.setTime(services.getTimeService());

        return servicesDTO;
    }
}
