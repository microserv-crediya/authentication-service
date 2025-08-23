package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.application.RolService;
import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.infraestructure.controllers.dto.rol.RolRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.rol.RolResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rol")
public class RolController {

    private final RolService rolService;
    private final Logger log =  LoggerFactory.getLogger(RolController.class);
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RolResponseDTO> createUser(@RequestBody RolRequestDTO dto) {
        log.info("*****Petición POST para registrar un nuevo rol recibida.");
        return rolService.createRol(dto.toRol())
                .map(RolResponseDTO::fromRol);
    }

    @GetMapping("/{id}")
    public Mono<RolResponseDTO> findById(@PathVariable UUID id) {
        return rolService.findById(id).map(RolResponseDTO::fromRol);
    }

    @GetMapping
    public Flux<Rol> findAll() {
        return rolService.findAll();
    }

    @PutMapping("/{id}")
    public Mono<RolResponseDTO> updateUser(@PathVariable UUID id, @RequestBody RolRequestDTO dto) {
        log.info("*****Petición PUT para actualizar rol.");
        Rol rol = dto.toRol();
        rol.setId(id); // Asignamos el ID antes de pasar al servicio
        return rolService.updatedRol(rol)
                .map(RolResponseDTO::fromRol);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable UUID id) {
        log.info("*****Petición DELETE para eliminar un rol.");
        return rolService.deleteById(id);
    }
}