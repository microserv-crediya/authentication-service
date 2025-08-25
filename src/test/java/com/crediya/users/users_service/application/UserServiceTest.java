package com.crediya.users.users_service.application;

import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_whenEmailNotExist() {
        // Arrange
        User user = new User(null, "Jhon", "Caraballo", "Manga Calle Real", "316456789","jhon.caraballo@example.com",  new BigDecimal("1500000"),LocalDate.parse("2000-05-10"),"8888888888");

        // Simula que el correo electrónico no existe
        when(userRepositoryPort.existsByCorreoElectronico(user.getCorreoElectronico())).thenReturn(Mono.just(false));
        // Simula el guardado del usuario
        when(userRepositoryPort.save(any(User.class))).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userService.createUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void createUser_whenEmailExists() {
        // Arrange
        User existingUser = new User(UUID.randomUUID(), "Yudis", "Cabarcas",  "AV Calle el manglar", "310654321", "yudis.cabarcas@example.com", new BigDecimal("1000000"), LocalDate.parse("2000-05-10"),"7777777777");

        // Simula que el correo electrónico ya existe en la base de datos
        when(userRepositoryPort.existsByCorreoElectronico(existingUser.getCorreoElectronico())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(userService.createUser(existingUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El correo electrónico ya está registrado."))
                .verify();
    }

    private User createValidUser() {
        return new User(
                UUID.randomUUID(),
                "Jhon", // Nombres actualizados
                "Caraballo", // Apellidos actualizados
                "Calle 123",
                "3001234567",
                "jhon.caraballo@email.com", // Correo actualizado
                new BigDecimal("5000000"),
                LocalDate.of(1990, 5, 15),
                "1234567890"
        );
    }

    @Test
    void shouldRevertTransactionWhenSubsequentOperationFails() {
        // Arrange
        User user = createValidUser();

        // 1. Simula el éxito al guardar el usuario
        when(userRepositoryPort.save(any(User.class))).thenReturn(Mono.just(user));

        // 2. Simula una operación que falla después de guardar (ej. la búsqueda)
        when(userRepositoryPort.findById(any(UUID.class)))
                .thenReturn(Mono.error(new RuntimeException("Error simulado en la segunda operación.")));

        // Act & Assert
        StepVerifier.create(userService.registerUserTransact(user))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error simulado en la segunda operación."))
                .verify();
    }
}