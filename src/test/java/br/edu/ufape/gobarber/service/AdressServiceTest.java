package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Address;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AdressServiceTest {


    @Test
    public void testAddressCreation() {
        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");

        assertEquals(1, address.getIdAddress());
        assertEquals("Rua A", address.getStreet());
        assertEquals(123, address.getNumber());
        assertEquals("Bairro B", address.getNeighborhood());
        assertEquals("Cidade C", address.getCity());
        assertEquals("Estado D", address.getState());
        assertEquals("12345-678", address.getCep());
    }

    @Test
    public void testAddressSetterMethods() {
        Address address = new Address();
        address.setStreet("Rua B");
        address.setNumber(456);

        assertEquals("Rua B", address.getStreet());
        assertEquals(456, address.getNumber());
    }

    @Test
    public void testInvalidCepFormat() {
        Address address = new Address();
        address.setCep("invalid-cep");

        assertNotEquals("12345-678", address.getCep());
    }

    @Test
    public void testAddressInequality() {
        Address address1 = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
        Address address2 = new Address(2, "Rua B", 456, "Bairro C", "Cidade D", "Estado E", "98765-432");

        assertNotEquals(address1, address2);
        assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void testDefaultConstructor() {
        Address address = new Address();

        assertNull(address.getIdAddress());
        assertNull(address.getStreet());
        assertNull(address.getNumber());
        assertNull(address.getNeighborhood());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getCep());
    }

    @Test
    public void testAddressAttributeModification() {
        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");


        address.setStreet("Rua Modificada");
        address.setNumber(999);
        address.setNeighborhood("Bairro Modificado");
        address.setCity("Cidade Modificada");
        address.setState("Estado Modificado");
        address.setCep("87654-321");


        assertEquals("Rua Modificada", address.getStreet());
        assertEquals(999, address.getNumber());
        assertEquals("Bairro Modificado", address.getNeighborhood());
        assertEquals("Cidade Modificada", address.getCity());
        assertEquals("Estado Modificado", address.getState());
        assertEquals("87654-321", address.getCep());
    }

    @Test
    public void testNonNullState() {
        Address address = new Address();
        address.setState("São Paulo");

        assertNotNull(address.getState());
        assertEquals("São Paulo", address.getState());
    }

    @Test
    public void testNonNullNumber() {
        Address address = new Address();
        address.setNumber(101);

        assertNotNull(address.getNumber());
        assertEquals(101, address.getNumber());
    }

    @Test
    public void testNeighborhoodAssignment() {
        Address address = new Address();
        address.setNeighborhood("Jardim das Flores");

        assertEquals("Jardim das Flores", address.getNeighborhood());
    }

    @Test
    public void testValidCepFormat() {
        Address address = new Address();
        address.setCep("98765-432");

        // Assume-se que o formato válido é "xxxxx-xxx"
        assertTrue(address.getCep().matches("\\d{5}-\\d{3}"));
    }
}
