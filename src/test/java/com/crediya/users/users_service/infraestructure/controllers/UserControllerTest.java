package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void createUser_withValidData() {
        // Arrange: Crea un objeto DTO con los datos del nuevo usuario para la petición.
        UserRequestDTO newUserRequestDTO = new UserRequestDTO(
                "Juan",
                "Sepulveda",
                "Calle colombia",
                "321234567",
                "juan.sepulveda@example.com",
                new BigDecimal("1500000"),
                LocalDate.parse("1987-10-04")
        );

        // Act & Assert: Realiza la petición HTTP y verifica la respuesta.
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
                    assert "Jhon".equals(responseDTO.getNombres());
                    assert "jhon.caraballo@example.com".equals(responseDTO.getCorreoElectronico());
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
                LocalDate.parse("2005-03-27")
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
}
