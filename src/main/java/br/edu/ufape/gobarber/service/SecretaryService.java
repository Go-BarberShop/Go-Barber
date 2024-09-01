package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import br.edu.ufape.gobarber.dto.page.PageSecretaryDTO;
import br.edu.ufape.gobarber.dto.secretary.SecretaryCreateDTO;
import br.edu.ufape.gobarber.dto.secretary.SecretaryDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Secretary;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SecretaryService {

    private final SecretaryRepository secretaryRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AddressService addressService;

    @Value("${jwt.secret}")
    private String secret;

    @Transactional
    public SecretaryDTO createSecretary(@Valid SecretaryCreateDTO secretaryCreateDTO, MultipartFile profilePhoto) throws DataBaseException {
        try {

            AddressCreateDTO addressCreateDTO = secretaryCreateDTO.getAddress();

            Address address = addressService.creatAddress(addressCreateDTO);

            Secretary secretary = convertCreateDTOtoEntity(secretaryCreateDTO);

            secretary.setUser(userRepository.save(secretary.getUser()));
            secretary.setAddress(address);

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                secretary.setProfilePhoto(profilePhoto.getBytes());
            }

            return convertToCompleteDTO(secretaryRepository.save(secretary));
        } catch (IOException e) {
            throw new DataBaseException("Erro ao processar a foto de perfil.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public SecretaryDTO updateSecretary(Integer id, SecretaryCreateDTO secretaryCreateDTO, MultipartFile profilePhoto) throws DataBaseException {
        Secretary secretary = secretaryRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Secretária não encontrada no banco de dados"));

        AddressCreateDTO addressCreateDTO = secretaryCreateDTO.getAddress();
        Address address = secretary.getAddress();
        address.setStreet(addressCreateDTO.getStreet());
        address.setNumber(addressCreateDTO.getNumber());
        address.setNeighborhood(addressCreateDTO.getNeighborhood());
        address.setCity(addressCreateDTO.getCity());
        address.setState(addressCreateDTO.getState());
        address.setCep(addressCreateDTO.getCep());

        address = addressRepository.save(address);

        secretary.setName(secretaryCreateDTO.getName());
        secretary.setCpf(secretaryCreateDTO.getCpf());
        secretary.setAddress(address);
        secretary.setSalary(secretaryCreateDTO.getSalary());
        secretary.setAdmissionDate(secretaryCreateDTO.getAdmissionDate());
        secretary.setWorkload(secretaryCreateDTO.getWorkload());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                secretary.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new DataBaseException("Erro ao processar a foto de perfil.");
            }
        }
        return convertToCompleteDTO(secretaryRepository.save(secretary));
    }

    @Transactional
    public void deleteSecretary(Integer id) {
        Optional<Secretary> secretary = secretaryRepository.findById(id);
        secretary.ifPresent(secretaryRepository::delete);
    }

    public SecretaryDTO getSecretary(Integer id) throws DataBaseException {
        return convertToCompleteDTO(secretaryRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Secretária não encontrada!")));
    }

    public SecretaryDTO getSecretary(HttpServletRequest request) throws DataBaseException {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findById(getJtiFromToken(token));

        if (user.isPresent()){
            Optional<Secretary> secretary = secretaryRepository.findByUser(user.get());

            if(secretary.isPresent()) {
                return convertToCompleteDTO(secretary.get());
            }

        }
        throw new DataBaseException("Não existe perfil de secretária associado a este login!");
    }

    public PageSecretaryDTO getAllSecretaries(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Secretary> secretary =  secretaryRepository.findAll(pageable);
        Page<SecretaryDTO> secretaryPage = secretary.map(this::convertToCompleteDTO);

        return new PageSecretaryDTO(
                secretaryPage.getTotalElements(),
                secretaryPage.getTotalPages(),
                secretaryPage.getPageable().getPageNumber(),
                secretaryPage.getSize(),
                secretaryPage.getContent()
        );
    }

    public byte[] getProfilePhoto(Integer id) throws DataBaseException {
        Secretary secretary = secretaryRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Secretária não encontrada!"));

        return secretary.getProfilePhoto();
    }

    public byte[] getProfilePhoto(HttpServletRequest request) throws DataBaseException {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findById(getJtiFromToken(token));

        if (user.isPresent()){
            Optional<Secretary> secretary = secretaryRepository.findByUser(user.get());

            if(secretary.isPresent()) {
                return secretary.get().getProfilePhoto();
            }

        }
        throw new DataBaseException("Não existe perfil de secretária associado a este login!");
    }

    private Secretary convertCreateDTOtoEntity(SecretaryCreateDTO secretaryCreateDTO) throws DataBaseException {
        Secretary secretary = new Secretary();
        secretary.setName(secretaryCreateDTO.getName());
        secretary.setCpf(secretaryCreateDTO.getCpf());

        secretary.setSalary(secretaryCreateDTO.getSalary());
        secretary.setAdmissionDate(secretaryCreateDTO.getAdmissionDate());
        secretary.setWorkload(secretaryCreateDTO.getWorkload());
        secretary.setContact(secretaryCreateDTO.getContact());

        LocalTime time = LocalTime.parse(secretaryCreateDTO.getStart(), DateTimeFormatter.ofPattern("HH:mm"));

        secretary.setStart(time);

        time = LocalTime.parse(secretaryCreateDTO.getEnd(), DateTimeFormatter.ofPattern("HH:mm"));

        secretary.setEnd(time);

        User user = new User();
        user.setLogin(secretaryCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(secretaryCreateDTO.getPassword()));
        user.setRole(roleService.findRoleByNome("ROLE_SECRETARY"));
        secretary.setUser(user);

        return secretary;
    }

    private SecretaryDTO convertToCompleteDTO(Secretary secretary){
        SecretaryDTO dto = new SecretaryDTO();

        dto.setIdSecretary(secretary.getIdSecretary());
        dto.setName(secretary.getName());
        dto.setCpf(secretary.getCpf());

        // Converter Address para AddressDTO, supondo que você tenha um método para isso
        dto.setAddress(secretary.getAddress());

        dto.setSalary(secretary.getSalary());
        dto.setAdmissionDate(secretary.getAdmissionDate());
        dto.setWorkload(secretary.getWorkload());
        dto.setContact(secretary.getContact());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeString;

        if(secretary.getStart() != null) {
            timeString = secretary.getStart().format(formatter);
        dto.setStart(timeString);
        }

        if(secretary.getEnd() != null) {
            timeString = secretary.getEnd().format(formatter);
            dto.setEnd(timeString);
        }

        dto.setEmail(secretary.getUser().getLogin());

        return dto;
    }

    private Integer getJtiFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody();
        return Integer.parseInt(claims.getId()); // jti é armazenado como ID no Claims
    }
}
