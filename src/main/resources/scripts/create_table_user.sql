CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombres VARCHAR(255),
    apellidos VARCHAR(255),
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    correo_electronico VARCHAR(255) UNIQUE,
    salario_base DECIMAL(10, 2),
    fecha_nacimiento DATE
);