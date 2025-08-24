package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void createUser_withValidData() {
        // Arrange
        UserRequestDTO newUserRequestDTO = new UserRequestDTO(
                "Juan", // Nombre que envías
                "Sepulveda",
                "Calle colombia",
                "321234567",
                "juan.sepulveda@example.com", // Correo que envías
                new BigDecimal("1500000"),
                LocalDate.parse("1987-10-04"),
                "999999999"
        );

        // Act & Assert
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUserRequestDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseDTO.class)
                .consumeWith(response -> {
                    UserResponseDTO responseDTO = response.getResponseBody();
                    assert responseDTO != null;
                    // La aserción debe coincidir con el valor de la variable `newUserRequestDTO`
                    assertEquals("Juan", responseDTO.getNombres());
                    assertEquals("juan.sepulveda@example.com", responseDTO.getCorreoElectronico());
                });
    }


    @Test
    void createUser_withInvalidEmail() {
        // Arrange: Crea un DTO con un correo electrónico inválido.
        UserRequestDTO newUserRequestDTO = new UserRequestDTO(
                "Cristina",
                "Soler",
                "Salvador de Bahia",
                "987654321",
                "cristina.soler", // Correo electrónico inválido
                new BigDecimal("1000000"),
                LocalDate.parse("2005-03-27"),
                "1010101010"
        );

        // Act & Assert: Realiza la petición y verifica que se retorne un 400 Bad Request.
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUserRequestDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class) // <--- ¡AQUÍ ESTÁ EL CAMBIO! Espera una cadena de texto
                .isEqualTo("Formato de correo electrónico inválido.");
                //.expectBody()
                //.jsonPath("$.message").isEqualTo("Formato de correo electrónico inválido.");
    }

    @Test
    void createUser_withNullFields() {
        // Arrange: Crea un DTO con campos nulos para probar validaciones @NotNull
        UserRequestDTO invalidUserDTO = new UserRequestDTO(
                null, // Campo nulo
                "Caraballo",
                "Manga Calle Real",
                "316456789",
                "jhon.caraballo@example.com",
                new BigDecimal("1500000"),
                LocalDate.parse("2000-05-10"),
                "3333333333"
        );

        // Act & Assert: La petición debe fallar con 400 Bad Request
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUserDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createUser_withInvalidSalary() {
        // Arrange: Crea un DTO con un salario fuera del rango permitido
        UserRequestDTO invalidUserDTO = new UserRequestDTO(
                "Luis",
                "Perez",
                "Av Siempre Viva",
                "3101234567",
                "luis.perez@example.com",
                new BigDecimal("20000000"), // Salario inválido (ej. <= 0)
                LocalDate.parse("1995-12-10"),
                "2222222222"
        );

        // Act & Assert: La petición debe fallar con 400 Bad Request
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUserDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void findUserById_found() {
        // Arrange: Crea un usuario para asegurarte de que existe en la DB.
        UserRequestDTO userToCreate = new UserRequestDTO(
                "Carlos", // Nombre que envías
                "Gomez",
                "Calle 10",
                "3123456789",
                "carlos.gomez@example.com",
                new BigDecimal("1000000"),
                LocalDate.parse("1990-01-01"),
                "1111111111"
        );

        // Simula la creación y captura del ID
        UserResponseDTO createdUser = webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userToCreate), UserRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();

        assert createdUser != null;
        UUID userId = createdUser.getId();

        // Act & Assert: Busca el usuario por el ID
        webTestClient.get().uri("/api/v1/usuarios/{id}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .consumeWith(response -> {
                    UserResponseDTO foundUser = response.getResponseBody();
                    assert foundUser != null;
                    assertEquals(userId, foundUser.getId());
                    // La aserción debe coincidir con el nombre del usuario que creaste
                    assertEquals("Carlos", foundUser.getNombres());
                });
    }



}
