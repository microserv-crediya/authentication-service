package com.crediya.users.users_service.infraestructure.enities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

@Table("roles")
public class RolEntity {
    @Id
    @Column("id")
    private UUID id;

    private String nombre;

    private String descripcion;

    public RolEntity() { }

    public RolEntity(UUID id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    public void setId(UUID id) { this.id = id; }
    public String getNombre() {return nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public UUID getId() { return id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() { return descripcion;}



}
