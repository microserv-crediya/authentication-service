package com.crediya.users.users_service.aplication;

import com.crediya.users.users_service.aplication.util.EmailValidator;
import com.crediya.users.users_service.domain.model.User;
import com.crediya.users.users_service.domain.repository.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional // Aplica la transaccionalidad a todos los métodos del servicio
public class UserService {

    private final UserRepositoryPort userRepositoryPort;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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


    public Mono<User> createUser(User usuario) {
        log.info("*****Iniciando el registro de un nuevo usuario con correo: {}", usuario.getCorreoElectronico());

        return validateUser(usuario)
                .then(userRepositoryPort.existsByCorreoElectronico(usuario.getCorreoElectronico()))
                .doOnError(e -> log.error("*****Error en existsByEmail: {}", e.getMessage())) // Aquí capturamos el error
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("*****Intento de registro con correo duplicado: {}", usuario.getCorreoElectronico());
                        return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado."));
                    }
                    // Agrega un log justo antes de guardar el usuario
                    log.info("*****Guardando usuario.");
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
