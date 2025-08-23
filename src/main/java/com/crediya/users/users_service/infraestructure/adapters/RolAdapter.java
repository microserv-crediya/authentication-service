package com.crediya.users.users_service.infraestructure.adapters;

import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.domain.repository.RolRepositoryPort;
import com.crediya.users.users_service.infraestructure.adapters.postgresql.RolRepository;
import com.crediya.users.users_service.infraestructure.enities.RolEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class RolAdapter implements RolRepositoryPort {

    private final RolRepository rolRepository;

    public RolAdapter(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }


    @Override
    public Mono<Rol> findById(UUID id) {
        return rolRepository.findById(id)
                .map(this::toDomain); // Convierte la entidad encontrada a un modelo de dominio
    }

    @Override
    public Flux<Rol> findAll() {
        return rolRepository.findAll()
                .map(this::toDomain); // Convierte la entidad encontrada a un modelo de dominio
    }

    @Override
    public Mono<Rol> save(Rol rol) {
        return Mono.just(rol)
                .map(this::toEntity) // Convierte el modelo de dominio a una entidad de infraestructura
                .flatMap(rolRepository::save) // Guarda la entidad en la base de datos
                .map(this::toDomain); // Convierte la entidad guardada de vuelta a un modelo de dominio
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return rolRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> findByNombre(String nombre) {
        return this.rolRepository.findByNombre(nombre);
    }



    // --- Métodos de Mapeo ---
    private RolEntity toEntity(Rol rol) {
        RolEntity entity = new RolEntity();
        entity.setId(rol.getId());
        entity.setNombre(rol.getNombre());
        entity.setDescripcion(rol.getDescripcion());
        return entity;
    }

    private Rol toDomain(RolEntity entity) {
        Rol rol = new Rol();
        rol.setId(entity.getId());
        rol.setNombre(entity.getNombre());
        rol.setDescripcion(entity.getDescripcion());
        return rol;
    }



}
