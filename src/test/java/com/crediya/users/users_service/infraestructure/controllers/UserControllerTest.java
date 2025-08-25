package com.crediya.users.users_service.infraestructure.controllers;

import com.crediya.users.users_service.application.UserService;
import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserRequestDTO testUserRequestDTO;
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

        testUserRequestDTO = new UserRequestDTO();
        // Asumiendo que UserRequestDTO tiene métodos setters similares
        testUserRequestDTO.setNombres("Juan Carlos");
        testUserRequestDTO.setApellidos("Pérez González");
        testUserRequestDTO.setCorreoElectronico("juan.perez@test.com");
        testUserRequestDTO.setDocumentoIdentidad("1234567890");
        testUserRequestDTO.setSalarioBase(new BigDecimal("800000"));
    }

    @Test
    @DisplayName("Debe obtener usuario por ID exitosamente")
    void shouldGetUserByIdSuccessfully() {
        // Given
        when(userService.findById(testUserId)).thenReturn(Mono.just(testUser));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/{id}", testUserId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(testUserId.toString())
                .jsonPath("$.nombres").isEqualTo("Juan Carlos")
                .jsonPath("$.apellidos").isEqualTo("Pérez González")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@test.com")
                .jsonPath("$.documentoIdentidad").isEqualTo("1234567890");
    }



    @Test
    @DisplayName("Debe obtener todos los usuarios exitosamente")
    void shouldGetAllUsersSuccessfully() {
        // Given
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setNombres("María");
        user2.setApellidos("García");
        user2.setCorreoElectronico("maria.garcia@test.com");
        user2.setDocumentoIdentidad("0987654321");
        user2.setSalarioBase(new BigDecimal("900000"));

        when(userService.findAll()).thenReturn(Flux.just(testUser, user2));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(2);
    }

    @Test
    @DisplayName("Debe crear usuario exitosamente")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userService.createUser(any(User.class))).thenReturn(Mono.just(testUser));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Juan Carlos")
                .jsonPath("$.apellidos").isEqualTo("Pérez González")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@test.com");
    }

    @Test
    @DisplayName("Debe retornar 400 al crear usuario con datos inválidos")
    void shouldReturn400WhenCreatingUserWithInvalidData() {
        // Given
        when(userService.createUser(any(User.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Datos de usuario inválidos")));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe actualizar usuario exitosamente")
    void shouldUpdateUserSuccessfully() {
        // Given
        User updatedUser = new User();
        updatedUser.setId(testUserId);
        updatedUser.setNombres("Juan Carlos Actualizado");
        updatedUser.setApellidos("Pérez González");
        updatedUser.setCorreoElectronico("juan.perez@test.com");
        updatedUser.setDocumentoIdentidad("1234567890");
        updatedUser.setSalarioBase(new BigDecimal("850000"));

        when(userService.updateUser(any(User.class))).thenReturn(Mono.just(updatedUser));

        // When & Then
        webTestClient.put()
                .uri("/api/v1/usuarios/{id}", testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(testUserId.toString())
                .jsonPath("$.nombres").isEqualTo("Juan Carlos Actualizado");
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar usuario inexistente")
    void shouldReturn404WhenUpdatingNonExistentUser() {
        // Given
        when(userService.updateUser(any(User.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("El usuario a actualizar no existe.")));

        // When & Then
        webTestClient.put()
                .uri("/api/v1/usuarios/{id}", testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe eliminar usuario exitosamente")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userService.deleteById(testUserId)).thenReturn(Mono.empty());

        // When & Then
        webTestClient.delete()
                .uri("/api/v1/usuarios/{id}", testUserId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("Debe verificar existencia de usuario por documento exitosamente")
    void shouldCheckUserExistsByDocumentSuccessfully() {
        // Given
        String documento = "1234567890";
        when(userService.checkUserExistsByDocumento(documento)).thenReturn(Mono.just(true));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/existe/{documentoIdentidad}", documento)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    @DisplayName("Debe retornar false cuando usuario no existe por documento")
    void shouldReturnFalseWhenUserDoesNotExistByDocument() {
        // Given
        String documento = "9999999999";
        when(userService.checkUserExistsByDocumento(documento)).thenReturn(Mono.just(false));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/existe/{documentoIdentidad}", documento)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);
    }

    @Test
    @DisplayName("Debe verificar existencia de usuario por email exitosamente")
    void shouldCheckUserExistsByEmailSuccessfully() {
        // Given
        String email = "juan.perez@test.com";
        when(userService.checkUserExistsByEmail(email)).thenReturn(Mono.just(true));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    @DisplayName("Debe retornar false cuando usuario no existe por email")
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {
        // Given
        String email = "noexiste@test.com";
        when(userService.checkUserExistsByEmail(email)).thenReturn(Mono.just(false));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);
    }

    @Test
    @DisplayName("Debe manejar errores internos del servidor")
    void shouldHandleInternalServerError() {
        // Given
        when(userService.findById(testUserId))
                .thenReturn(Mono.error(new RuntimeException("Error interno del servidor")));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/usuarios/{id}", testUserId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}