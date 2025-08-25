package com.crediya.users.users_service.application;

import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(testUserId);
        testUser.setNombres("Juan Carlos");
        testUser.setApellidos("Pérez González");
        testUser.setCorreoElectronico("juan.perez@test.com");
        testUser.setDocumentoIdentidad("1234567890");
        testUser.setSalarioBase(new BigDecimal("800000"));
    }

    @Test
    @DisplayName("Debe encontrar usuario por ID exitosamente")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.findById(testUserId);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Debe retornar Mono vacío cuando usuario no existe por ID")
    void shouldReturnEmptyMonoWhenUserNotFoundById() {
        // Given
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.empty());

        // When
        Mono<User> result = userService.findById(testUserId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios exitosamente")
    void shouldFindAllUsersSuccessfully() {
        // Given
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setNombres("María");
        user2.setApellidos("García");
        user2.setCorreoElectronico("maria.garcia@test.com");
        user2.setDocumentoIdentidad("0987654321");
        user2.setSalarioBase(new BigDecimal("900000"));

        when(userRepositoryPort.findAll()).thenReturn(Flux.just(testUser, user2));

        // When
        Flux<User> result = userService.findAll();

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .expectNext(user2)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe crear usuario exitosamente")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepositoryPort.existsByCorreoElectronico(testUser.getCorreoElectronico()))
                .thenReturn(Mono.just(false));
        when(userRepositoryPort.existsByDocumentoIdentidad(testUser.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(userRepositoryPort.save(any(User.class))).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(testUser.getCorreoElectronico());
        verify(userRepositoryPort, times(1)).existsByDocumentoIdentidad(testUser.getDocumentoIdentidad());
        verify(userRepositoryPort, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Debe fallar al crear usuario con correo duplicado")
    void shouldFailToCreateUserWithDuplicateEmail() {
        // Given
        when(userRepositoryPort.existsByCorreoElectronico(testUser.getCorreoElectronico()))
                .thenReturn(Mono.just(true));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El correo electrónico ya está registrado."))
                .verify();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(testUser.getCorreoElectronico());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe fallar al crear usuario con documento duplicado")
    void shouldFailToCreateUserWithDuplicateDocument() {
        // Given
        when(userRepositoryPort.existsByCorreoElectronico(testUser.getCorreoElectronico()))
                .thenReturn(Mono.just(false));
        when(userRepositoryPort.existsByDocumentoIdentidad(testUser.getDocumentoIdentidad()))
                .thenReturn(Mono.just(true));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El documento de identidad ya está registrado."))
                .verify();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(testUser.getCorreoElectronico());
        verify(userRepositoryPort, times(1)).existsByDocumentoIdentidad(testUser.getDocumentoIdentidad());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe crear usuario sin validar documento cuando está vacío")
    void shouldCreateUserWithoutValidatingEmptyDocument() {
        // Given
        testUser.setDocumentoIdentidad(""); // Documento vacío
        when(userRepositoryPort.existsByCorreoElectronico(testUser.getCorreoElectronico()))
                .thenReturn(Mono.just(false));
        when(userRepositoryPort.save(any(User.class))).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(testUser.getCorreoElectronico());
        verify(userRepositoryPort, never()).existsByDocumentoIdentidad(anyString());
        verify(userRepositoryPort, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Debe crear usuario sin validar documento cuando es null")
    void shouldCreateUserWithoutValidatingNullDocument() {
        // Given
        testUser.setDocumentoIdentidad(null); // Documento null
        when(userRepositoryPort.existsByCorreoElectronico(testUser.getCorreoElectronico()))
                .thenReturn(Mono.just(false));
        when(userRepositoryPort.save(any(User.class))).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(testUser.getCorreoElectronico());
        verify(userRepositoryPort, never()).existsByDocumentoIdentidad(anyString());
        verify(userRepositoryPort, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Debe fallar al crear usuario con datos inválidos")
    void shouldFailToCreateUserWithInvalidData() {
        // Given - Usuario con nombres vacíos (inválido según isValid())
        testUser.setNombres("");

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Datos de usuario inválidos"))
                .verify();

        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe fallar al crear usuario con email inválido")
    void shouldFailToCreateUserWithInvalidEmail() {
        // Given
        testUser.setCorreoElectronico("email-invalido");

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Formato de correo electrónico inválido."))
                .verify();

        verify(userRepositoryPort, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Debe fallar al crear usuario con salario inválido - mayor que 1,500,000")
    void shouldFailToCreateUserWithInvalidSalaryAboveLimit() {
        // Given
        testUser.setSalarioBase(new BigDecimal("1600000"));

        // When
        Mono<User> result = userService.createUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("El salario base debe ser un valor numérico entre 0 y 1.500.000"))
                .verify();

        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe actualizar usuario exitosamente")
    void shouldUpdateUserSuccessfully() {
        // Given
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.just(testUser));
        when(userRepositoryPort.save(testUser)).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.updateUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).findById(testUserId);
        verify(userRepositoryPort, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Debe fallar al actualizar usuario sin ID")
    void shouldFailToUpdateUserWithoutId() {
        // Given
        testUser.setId(null);

        // When
        Mono<User> result = userService.updateUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El ID del usuario es requerido para la actualización."))
                .verify();

        verify(userRepositoryPort, never()).findById(any());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe fallar al actualizar usuario inexistente")
    void shouldFailToUpdateNonExistentUser() {
        // Given
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.empty());

        // When
        Mono<User> result = userService.updateUser(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El usuario a actualizar no existe."))
                .verify();

        verify(userRepositoryPort, times(1)).findById(testUserId);
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe eliminar usuario exitosamente")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepositoryPort.deleteById(testUserId)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = userService.deleteById(testUserId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).deleteById(testUserId);
    }

    @Test
    @DisplayName("Debe verificar existencia de usuario por documento exitosamente")
    void shouldCheckUserExistsByDocumentSuccessfully() {
        // Given
        String documento = "1234567890";
        when(userRepositoryPort.existsByDocumentoIdentidad(documento)).thenReturn(Mono.just(true));

        // When
        Mono<Boolean> result = userService.checkUserExistsByDocumento(documento);

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).existsByDocumentoIdentidad(documento);
    }

    @Test
    @DisplayName("Debe verificar existencia de usuario por email exitosamente")
    void shouldCheckUserExistsByEmailSuccessfully() {
        // Given
        String email = "juan.perez@test.com";
        when(userRepositoryPort.existsByCorreoElectronico(email)).thenReturn(Mono.just(true));

        // When
        Mono<Boolean> result = userService.checkUserExistsByEmail(email);

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).existsByCorreoElectronico(email);
    }

    @Test
    @DisplayName("Debe registrar usuario con transacción exitosamente")
    void shouldRegisterUserTransactSuccessfully() {
        // Given
        User savedUser = new User();
        savedUser.setId(testUserId);
        savedUser.setNombres(testUser.getNombres());
        savedUser.setApellidos(testUser.getApellidos());
        savedUser.setCorreoElectronico(testUser.getCorreoElectronico());

        when(userRepositoryPort.save(testUser)).thenReturn(Mono.just(savedUser));
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.just(savedUser));

        // When
        Mono<User> result = userService.registerUserTransact(testUser);

        // Then
        StepVerifier.create(result)
                .expectNext(savedUser)
                .verifyComplete();

        verify(userRepositoryPort, times(1)).save(testUser);
        verify(userRepositoryPort, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Debe fallar transacción cuando usuario no se encuentra después de guardar")
    void shouldFailTransactionWhenUserNotFoundAfterSave() {
        // Given
        User savedUser = new User();
        savedUser.setId(testUserId);

        when(userRepositoryPort.save(testUser)).thenReturn(Mono.just(savedUser));
        when(userRepositoryPort.findById(testUserId)).thenReturn(Mono.empty());

        // When
        Mono<User> result = userService.registerUserTransact(testUser);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals("El usuario no se encontró después de guardar."))
                .verify();

        verify(userRepositoryPort, times(1)).save(testUser);
        verify(userRepositoryPort, times(1)).findById(testUserId);
    }
}