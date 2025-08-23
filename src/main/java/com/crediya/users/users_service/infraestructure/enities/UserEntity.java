package com.crediya.users.users_service.infraestructure.enities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("users")
public class UserEntity {

    @Id
    private UUID id;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;


    //Constructor vacío, necesario para el mapeo con Spring Data
    public UserEntity() {}

    //Constructor completo
    public UserEntity(UUID id, String nombres, String apellidos, String direccion, String telefono, String correoElectronico, BigDecimal salarioBase, LocalDate fechaDeNacimiento) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.fechaNacimiento = fechaDeNacimiento;
    }

    public void setId(UUID id) {  this.id = id;  }
    public void setNombres(String nombres) {   this.nombres = nombres;  }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase;  }

    public UUID getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono;  }
    public String getCorreoElectronico() { return correoElectronico; }
    public BigDecimal getSalarioBase() { return salarioBase; }
}
