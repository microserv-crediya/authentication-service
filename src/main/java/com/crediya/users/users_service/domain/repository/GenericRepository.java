package com.crediya.users.users_service.domain.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenericRepository <T , ID>  {
    //Crear o actualizar un registro
    Mono<T> save(T domainModel);

    //Leer un registro por su ID
    Mono<T> findById(ID id);

    //Leer todos los registros
    Flux<T> findAll();

    //Eliminar un registro por su ID
    Mono<Void> deleteById(ID id);
}
