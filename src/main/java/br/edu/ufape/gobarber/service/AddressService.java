package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public Address creatAddress(Address address) throws DataBaseException {
        try {
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new DataBaseException("Erro ao criar endereço: " + e.getMessage());
        }
    }

    @Transactional
    public Address updateAddress(Integer id, Address newAddress) throws DataBaseException {
        Address address = addressRepository.findById(id).orElseThrow(() -> new DataBaseException("Endereço não encontrado"));

        address.setStreet(newAddress.getStreet());
        address.setNumber(newAddress.getNumber());
        address.setNeighborhood(newAddress.getNeighborhood());
        address.setCity(newAddress.getCity());
        address.setState(newAddress.getState());
        address.setCep(newAddress.getCep());

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Integer id) throws DataBaseException {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Endereço não encontrado."));
        addressRepository.delete(address);
    }

    public Address getAddressById(Integer id) throws DataBaseException {
        return addressRepository.findById(id).orElseThrow(() -> new DataBaseException("Endereço não encontrado."));
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

}