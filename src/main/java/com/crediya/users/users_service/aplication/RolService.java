package com.crediya.users.users_service.aplication;


import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.domain.repository.RolRepositoryPort;
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
public class RolService {

    private final RolRepositoryPort rolRepositoryPort;
    private static final Logger log = LoggerFactory.getLogger(RolService.class);

    public RolService(RolRepositoryPort rolRepositoryPort) {
        this.rolRepositoryPort = rolRepositoryPort;
    }


    /**
     * Busca un rol por su ID.
     *
     * @param id El ID del rol a buscar.
     * @return Un Mono que emite el Rol encontrado.
     */
    public Mono<Rol> findById(UUID id) {
        return rolRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado con ID: " + id)));
    }

    /**
     * Busca todos los roles.
     *
     * @return Un Flux que emite todos los Roles.
     */
    public Flux<Rol> findAll() {
        return rolRepositoryPort.findAll();
    }


    /**
     * Crea un nuevo rol, validando que el nombre no exista.
     *
     * @param rol El objeto Rol a crear.
     * @return Un Mono que emite el Rol creado.
     */
    public Mono<Rol> createRol(Rol rol) {
        log.info("Iniciando la creación del rol: {}", rol.getNombre());
        return rolRepositoryPort.findByNombre(rol.getNombre())
                .flatMap(foundRol -> {
                    log.warn("Intento de creación de rol duplicado: {}", rol.getNombre());
                    return Mono.error(new IllegalArgumentException("El nombre de rol ya existe."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Guardando Rol...");
                    return rolRepositoryPort.save(rol);
                }))
                .cast(Rol.class)
                .doOnSuccess(savedRol -> log.info("Rol guardado exitosamente con ID: {}", savedRol.getId()))
                .doOnError(e -> log.error("Error al crear el rol: {}", e.getMessage()));
    }


    /**
     * Actualiza un rol existente.
     *
     * @param rol El objeto Rol con los datos actualizados.
     * @return Un Mono que emite el Rol actualizado.
     */
    public Mono<Rol> updatedRol(Rol rol) {
        log.info("Actualizando el rol con ID: {}", rol.getId());
        return rolRepositoryPort.findById(rol.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado para actualizar.")))
                .flatMap(existingRol -> {
                    log.info("Rol encontrado, actualizando datos...");
                    existingRol.setNombre(rol.getNombre());
                    existingRol.setDescripcion(rol.getDescripcion());
                    return rolRepositoryPort.save(existingRol);
                })
                .doOnSuccess(updatedRol -> log.info("Rol actualizado exitosamente: {}", updatedRol.getId()))
                .doOnError(e -> log.error("Error al actualizar el rol: {}", e.getMessage()));
    }


    /**
     * Elimina un rol por su ID.
     *
     * @param id El ID del rol a eliminar.
     * @return Un Mono vacío que indica la finalización.
     */
    public Mono<Void> deleteById(UUID id) {
        log.info("Eliminando el rol con ID: {}", id);
        return rolRepositoryPort.deleteById(id)
                .doOnSuccess(v -> log.info("Rol eliminado exitosamente."))
                .doOnError(e -> log.error("Error al eliminar el rol: {}", e.getMessage()));
    }


}
