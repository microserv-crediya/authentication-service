package com.crediya.users.users_service.infraestructure.adapters.postgresql;

import com.crediya.users.users_service.infraestructure.enities.RolEntity;
import com.crediya.users.users_service.infraestructure.enities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface RolRepository extends R2dbcRepository<RolEntity, UUID> {
    Mono<Boolean> findByNombre(String nombre);
}
