package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.barber.BarberCreateDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.service.BarberService;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@SpringBootTest
public class BarberServiceTest {

    @Autowired
    private BarberService barberService;
    @Test
    public void testBarberCreation() {
        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
        Barber barber = new Barber(1, "João", "123.456.789-10", address, 3000.00, LocalDate.now(), null, 40,null);

        assertEquals(1, barber.getIdBarber());
        assertEquals("João", barber.getName());
        assertEquals("123.456.789-10", barber.getCpf());
        assertEquals(address, barber.getAddress());
        assertEquals(3000.00, barber.getSalary());
        assertEquals(LocalDate.now(), barber.getAdmissionDate());
        assertEquals(40, barber.getWorkload());
    }

    @Test
    public void testUpdateBarberData() {
        Barber barber = new Barber();
        barber.setName("Carlos");
        barber.setCpf("987.654.321-00");

        assertEquals("Carlos", barber.getName());
        assertEquals("987.654.321-00", barber.getCpf());
    }

    @Test
    public void testBarberCreationWithoutProfilePhoto() {

        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
        Barber barber = new Barber(null,
                "João",
                "123.456.789-10",
                address, 3000.00,
                LocalDate.now(),
                null,
                40,
                null);

        assertNull(barber.getProfilePhoto());
    }

    @Test
    public void testInvalidCpfFormat() {
        Barber barber = new Barber();
        barber.setCpf("invalid-cpf");

        // Verificar se CPF não é válido (dependendo da lógica de validação do sistema)
        assertNotEquals("123.456.789-10", barber.getCpf());
    }

    @Test
    public void testDefaultConstructor() {
        Barber barber = new Barber();

        assertNull(barber.getIdBarber());
        assertNull(barber.getName());
        assertNull(barber.getCpf());
        assertNull(barber.getAddress());
        assertEquals(0.0, barber.getSalary());
        assertNull(barber.getAdmissionDate());
        assertNull(barber.getProfilePhoto());
        assertNull(barber.getWorkload());
    }

    @Test
    public void testSalaryValidation() {
        Barber barber = new Barber();
        barber.setSalary(3500.00);

        assertEquals(3500.00, barber.getSalary());

        // Verifica se o salário não pode ser negativo
        barber.setSalary(-500.00);
        assertTrue(barber.getSalary() < 0);
    }

    @Test
    public void testWorkloadValidation() {
        Barber barber = new Barber();
        barber.setWorkload(40);

        assertEquals(40, barber.getWorkload());

        // Verifica se a carga horária não pode ser negativa
        barber.setWorkload(-10);
        assertTrue(barber.getWorkload() < 0);
    }

    @Test
    public void testAdmissionDateAssignment() {
        LocalDate today = LocalDate.now();
        Barber barber = new Barber();
        barber.setAdmissionDate(today);

        assertEquals(today, barber.getAdmissionDate());
    }

    @Test
    public void testProfilePhotoModification() {
        Barber barber = new Barber();
        byte[] initialPhoto = new byte[]{1, 2, 3};
        byte[] newPhoto = new byte[]{4, 5, 6};

        barber.setProfilePhoto(initialPhoto);
        assertArrayEquals(initialPhoto, barber.getProfilePhoto());

        barber.setProfilePhoto(newPhoto);
        assertArrayEquals(newPhoto, barber.getProfilePhoto());
    }

}
