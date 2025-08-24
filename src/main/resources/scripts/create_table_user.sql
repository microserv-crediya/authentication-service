CREATE TABLE users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    	nombres varchar(160) NULL,
    	apellidos varchar(200) NULL,
    	direccion varchar(255) NULL,
    	telefono varchar(100) NULL,
    	correo_electronico varchar(255) NULL,
    	salario_base numeric(10, 2) NULL,
    	fecha_nacimiento date NULL,
    	documento_identidad varchar(20) NULL,
    	CONSTRAINT users_correo_electronico_key UNIQUE (correo_electronico),
    	CONSTRAINT users_documento_identidad_key UNIQUE (documento_identidad), -- ¡Esta es la línea clave!
    	CONSTRAINT users_pkey PRIMARY KEY (id)
    );