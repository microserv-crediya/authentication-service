package com.crediya.users.users_service.infraestructure.controllers;


import com.crediya.users.users_service.application.UserService;
import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserRequestDTO;
import com.crediya.users.users_service.infraestructure.controllers.dto.user.UserResponseDTO;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        log.info("*****Petición POST para registrar un nuevo usuario recibida.");
        return userService.createUser(dto.toUser())
                .map(UserResponseDTO::fromUser);
    }

    @GetMapping("/{id}")
    public Mono<UserResponseDTO> findById(@PathVariable UUID id) {
        return userService.findById(id).map(UserResponseDTO::fromUser);
    }

    @GetMapping
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public Mono<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO dto) {
        log.info("*****Petición PUT para actualizar usuario.");
        User user = dto.toUser();
        user.setId(id); // Asignamos el ID antes de pasar al servicio
        return userService.updateUser(user)
                .map(UserResponseDTO::fromUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable UUID id) {
        log.info("*****Petición DELETE para eliminar un usuario.");
        return userService.deleteById(id);
    }
}