package com.crediya.users.users_service.aplication;

import com.crediya.users.users_service.aplication.util.EmailValidator;
import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.domain.repository.UserRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public class UserService {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }


    //Metodo para buscar usuario por su ID
    public Mono<User> findById(UUID id) {
        return userRepositoryPort.findById(id);
    }


    //Metodo para mostrar todos los usuarios.
    public Flux<User> findAll() {
        return userRepositoryPort.findAll();
    }


    public Mono<User> registrarUsuario(User usuario) {

        return  validateUser(usuario)
                .then(userRepositoryPort.existsByEmail(usuario.getCorreoElectronico()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado."));
                    }
                    return userRepositoryPort.save(usuario);
                });
    }


    //Metodo para actualizar un usuario existente.
    public Mono<User> updateUser(User usuario) {
        //Validar que el usuario exista antes de actualizar.
        if (usuario.getId() == null) {
            return Mono.error(new IllegalArgumentException("El ID del usuario es requerido para la actualización."));
        }

       return  validateUser(usuario)
               .then(userRepositoryPort.findById(usuario.getId()))
               .switchIfEmpty(Mono.error(new IllegalArgumentException("El usuario a actualizar no existe.")))
               .flatMap(existingUser -> userRepositoryPort.save(usuario));
    }


    //Metodo para eliminar usuario por su ID
    public Mono<Void> deleteById(UUID id) {
        return userRepositoryPort.deleteById(id);
    }


    //Metodo privado para agrupar todas las validaciones comunes
    private Mono<Void> validateUser(User usuario) {
        //1. Validación de datos a nivel de dominio
        if (!usuario.isValid()) {
            return Mono.error(new IllegalArgumentException("Datos de usuario inválidos, Los siguientes campos son obligatorios Nombres, Apellidos, Correo electronico y Salario base, ."));
        }

        //2. Validación de formato de email
        if (!EmailValidator.isValid(usuario.getCorreoElectronico())) {
            return Mono.error(new IllegalArgumentException("Formato de correo electrónico inválido."));
        }

        //3. Validación de salario (regla de aplicación)
        if (usuario.getSalarioBase() == null || usuario.getSalarioBase().compareTo(new BigDecimal("0")) <= 0 || usuario.getSalarioBase().compareTo(new BigDecimal("1500000")) > 0) {
            return Mono.error(new IllegalArgumentException("El salario base debe ser un valor numérico entre 0 y 1.500.000."));
        }

        return Mono.empty(); // Retorna un Mono vacío si todas las validaciones pasan
    }
}
