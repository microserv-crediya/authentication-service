package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.application.UserService;
import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserService userService;
    private final Logger log =  LoggerFactory.getLogger(UserController.class);
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Busca y retorna la información de un usuario específico utilizando su identificador único.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado y retornado exitosamente.",  content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    public Mono<UserResponseDTO> findById(@Parameter(description = "ID único del usuario.") @PathVariable UUID id) {
        return userService.findById(id).map(UserResponseDTO::fromUser);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista con todos los usuarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    public Flux<User> findAll() {
        return userService.findAll();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo usuario",description = "Registra un nuevo usuario en el sistema con datos válidos.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, como correo duplicado o datos inválidos.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    public Mono<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        log.info("*****Petición POST para registrar un nuevo usuario recibida.");
        return userService.createUser(dto.toUser())
                .map(UserResponseDTO::fromUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente", description = "Actualiza la información de un usuario específico. El ID del usuario debe coincidir con el ID en el cuerpo de la solicitud.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente.")
    @ApiResponse(responseCode = "400", description = "ID en la ruta no coincide con el ID en el cuerpo de la solicitud o datos inválidos.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    public Mono<UserResponseDTO> updateUser(@Parameter(description = "ID del usuario a actualizar.") @PathVariable UUID id, @RequestBody UserRequestDTO dto) {
        log.info("*****Petición PUT para actualizar usuario.");
        User user = dto.toUser();
        user.setId(id); // Asignamos el ID antes de pasar al servicio
        return userService.updateUser(user)
                .map(UserResponseDTO::fromUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema utilizando su ID. Retorna 204 si la eliminación es exitosa.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente. No se retorna contenido.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    public Mono<Void> deleteById(@Parameter(description = "ID único del usuario.")  @PathVariable UUID id) {
        log.info("*****Petición DELETE para eliminar un usuario.");
        return userService.deleteById(id);
    }

    @Operation(
            summary = "Verifica la existencia de un usuario",
            description = "Permite validar si un usuario existe en el sistema por su documento de identidad. Retorna 'true' si el usuario existe y 'false' si no.",
            tags = { "Gestión de Usuarios" }
    )
    @ApiResponse(responseCode = "200", description = "Validación exitosa")
    @Parameter(
            name = "documentoIdentidad",
            description = "Documento de identidad del usuario a verificar",
            example = "1234567890"
    )
    @GetMapping("/existe/{documentoIdentidad}")
    public Mono<Boolean> checkUserExists(@PathVariable String documentoIdentidad) {
        return userService.checkUserExistsByDocumento(documentoIdentidad);
    }

    @Operation(summary = "Obtener un usuario por Correo Electronico", description = "Busca y retorna la información de un usuario específico utilizando su identificador de Correo Electronico.")
    @Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la creación y gestión de usuarios.")
    @GetMapping("/email/{email}")
    public Mono<Boolean> checkUserExistsByEmail(@PathVariable String email) {
        return userService.checkUserExistsByEmail(email);
    }
}