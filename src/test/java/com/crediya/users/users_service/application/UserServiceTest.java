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
    void createUser_whenEmailDoesNotExist_shouldSaveUser() {
        // Arrange
        User user = new User(null, "Jhon", "Caraballo", "Manga Calle Real", "316456789", "jhon.caraballo@example.com",  new BigDecimal("1500000"), LocalDate.parse("2000-05-10"));

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
    void createUser_whenEmailAlreadyExists_shouldReturnError() {
        // Arrange
        User existingUser = new User(UUID.randomUUID(), "Yudis", "Cabarcas",  "AV Calle el manglar", "310654321", "yudis.cabarcas@example.com", new BigDecimal("1000000"), LocalDate.parse("2000-05-10"));

        // Simula que el correo electrónico ya existe en la base de datos
        when(userRepositoryPort.existsByCorreoElectronico(existingUser.getCorreoElectronico())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(userService.createUser(existingUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El correo electrónico ya está registrado."))
                .verify();
    }
}