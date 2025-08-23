package com.crediya.users.users_service.infraestructure.adapters.postgresql;

import com.crediya.users.users_service.infraestructure.enities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface  UserRepository extends R2dbcRepository<UserEntity, UUID> {
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
