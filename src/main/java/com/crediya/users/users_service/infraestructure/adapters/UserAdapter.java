package com.crediya.users.users_service.infraestructure.adapters;

import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.domain.repository.UserRepositoryPort;
import com.crediya.users.users_service.infraestructure.adapters.postgresql.UserRepository;
import com.crediya.users.users_service.infraestructure.enities.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    public UserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    @Override
    public Mono<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(this::toDomain); // Convierte la entidad encontrada a un modelo de dominio
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll()
                .map(this::toDomain); // Convierte la entidad encontrada a un modelo de dominio
    }

    @Override
    public Mono<User> save(User usuario) {
        return Mono.just(usuario)
                .map(this::toEntity) // Convierte el modelo de dominio a una entidad de infraestructura
                .flatMap(userRepository::save) // Guarda la entidad en la base de datos
                .map(this::toDomain); // Convierte la entidad guardada de vuelta a un modelo de dominio
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return userRepository.deleteById(id);
    }



    // --- Métodos de Mapeo ---
    private UserEntity toEntity(User usuario) {
        UserEntity entity = new UserEntity();
        entity.setId(usuario.getId());
        entity.setNombres(usuario.getNombres());
        entity.setApellidos(usuario.getApellidos());
        entity.setDireccion(usuario.getDireccion());
        entity.setTelefono(usuario.getTelefono());
        entity.setCorreoElectronico(usuario.getCorreoElectronico());
        entity.setSalarioBase(usuario.getSalarioBase());
        entity.setFechaNacimiento(usuario.getFechaNacimiento());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        User usuario = new User();
        usuario.setId(entity.getId());
        usuario.setNombres(entity.getNombres());
        usuario.setApellidos(entity.getApellidos());
        usuario.setDireccion(entity.getDireccion());
        usuario.setTelefono(entity.getTelefono());
        usuario.setCorreoElectronico(entity.getCorreoElectronico());
        usuario.setSalarioBase(entity.getSalarioBase());
        usuario.setFechaNacimiento(entity.getFechaNacimiento());
        return usuario;
    }


    @Override
    public Mono<Boolean> existsByCorreoElectronico(String correoElectronico) {
        return this.userRepository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad) {
        return this.userRepository.existsByDocumentoIdentidad(documentoIdentidad);

    }
}
