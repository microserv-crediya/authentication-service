package com.crediya.users.users_service.domain.repository;

import com.crediya.users.users_service.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepositoryPort extends GenericRepository <User, UUID> {

    //Metodo específico para el dominio de Usuario
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
