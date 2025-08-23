package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.application.RolService;
import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.infraestructure.controllers.dto.rol.RolRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.rol.RolResponseDTO;
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
@RequestMapping("/api/v1/rol")
@Tag(name = "Gestión de Roles", description = "Operaciones CRUD para la administración de roles de usuario.")
public class RolController {

    private final RolService rolService;
    private final Logger log = LoggerFactory.getLogger(RolController.class);

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo rol",
            description = "Registra un nuevo rol en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rol creado exitosamente.",
                            content = @Content(schema = @Schema(implementation = RolResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida. El rol ya existe o los datos son incorrectos.")
            }
    )
    public Mono<RolResponseDTO> createRol(@RequestBody RolRequestDTO dto) {
        log.info("*****Petición POST para registrar un nuevo rol recibida.");
        return rolService.createRol(dto.toRol()).map(RolResponseDTO::fromRol);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un rol por ID",
            description = "Busca y retorna un rol específico por su identificador único.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol encontrado y retornado."),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado.")
            }
    )
    public Mono<RolResponseDTO> findById(@Parameter(description = "ID del rol a buscar") @PathVariable UUID id) {
        return rolService.findById(id).map(RolResponseDTO::fromRol);
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los roles",
            description = "Retorna una lista de todos los roles registrados en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente.")
    public Flux<Rol> findAll() {
        return rolService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un rol por ID",
            description = "Actualiza la información de un rol existente. El ID en la URL debe coincidir con el rol a actualizar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente."),
                    @ApiResponse(responseCode = "400", description = "ID de la ruta no coincide con el cuerpo o datos inválidos."),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado.")
            }
    )
    public Mono<RolResponseDTO> updateRol(@Parameter(description = "ID del rol a actualizar") @PathVariable UUID id, @RequestBody RolRequestDTO dto) {
        log.info("*****Petición PUT para actualizar rol.");
        Rol rol = dto.toRol();
        rol.setId(id);
        return rolService.updatedRol(rol).map(RolResponseDTO::fromRol);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar un rol por ID",
            description = "Elimina un rol del sistema. Retorna 204 No Content si la eliminación es exitosa.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente."),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado.")
            }
    )
    public Mono<Void> deleteById(@Parameter(description = "ID del rol a eliminar") @PathVariable UUID id) {
        log.info("*****Petición DELETE para eliminar un rol.");
        return rolService.deleteById(id);
    }
}
