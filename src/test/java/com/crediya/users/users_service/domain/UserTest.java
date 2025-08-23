package com.crediya.users.users_service.domain;

import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.domain.model.User;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void crearUsuario_conDatosValidos_debeCrearInstanciaCorrecta() {
        // Arrange
        UUID id = UUID.randomUUID();
        String nombres = "Jhon";
        String apellidos = "Sepulveda";
        String correo = "jhon.seulveda@example.com";
        LocalDate fechaNacimiento = LocalDate.parse("1986-11-01");
        String direccion = "street # 34";
        String telefono = "555-1234";
        BigDecimal salarioBase = new BigDecimal("50000.00");
        //Rol rol = new Rol(UUID.randomUUID(), "CLIENTE", "Rol de cliente");

        // Act
        User user = new User(id, nombres, apellidos, direccion, telefono, correo, salarioBase, fechaNacimiento);

        // Assert
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(nombres, user.getNombres());
        assertEquals(apellidos, user.getApellidos());
        assertEquals(correo, user.getCorreoElectronico());
        assertEquals(fechaNacimiento, user.getFechaNacimiento());
        assertEquals(telefono, user.getTelefono());
        assertEquals(salarioBase, user.getSalarioBase());
        //assertEquals(rol, user.getRol());
    }

    @Test
    void setters_debenActualizarLosValoresCorrectamente() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Steve", "Xavi", "Calle la Matuna", "987654321", "steve.xavi@example.com", new BigDecimal("60000.00"), LocalDate.parse("1988-01-11"));

        // Act
        String nuevoNombre = "Steve";
        user.setNombres(nuevoNombre);
        String nuevoApellido = "Xavi";
        user.setApellidos(nuevoApellido);
        String nuevoEmail = "steve.xavi@example.com";
        user.setCorreoElectronico(nuevoEmail);
        //Rol nuevoRol = new Rol(UUID.randomUUID(), "ADMIN", "Rol de administrador");
        //user.setRol(nuevoRol);

        // Assert
        assertEquals(nuevoNombre, user.getNombres());
        assertEquals(nuevoApellido, user.getApellidos());
        assertEquals(nuevoEmail, user.getCorreoElectronico());
        //assertEquals(nuevoRol, user.getRol());
    }
}