package com.crediya.users.users_service.domain.repository;

import com.crediya.users.users_service.domain.model.Rol;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RolRepositoryPort extends GenericRepository <Rol, UUID> {
    Mono<Boolean> findByNombre(String nombre);
}
