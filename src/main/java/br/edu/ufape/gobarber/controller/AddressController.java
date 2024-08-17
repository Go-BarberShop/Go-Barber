package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.service.AddressService;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address address) throws DataBaseException {
        Address createdAddress = addressService.creatAddress(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody Address adressDetails) throws DataBaseException {
        Address updatedAddress = addressService.updateAddress(id, adressDetails);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) throws DataBaseException {
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) throws DataBaseException {
        Address address = addressService.getAddressById(id);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

}
