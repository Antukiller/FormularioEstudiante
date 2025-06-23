DROP TABLE IF EXISTS Estudiante;

CREATE TABLE Estudiante (
    id IDENTITY PRIMARY KEY,
    nia VARCHAR,
    curso VARCHAR,
    apellidos VARCHAR NOT NULL,
    nombre VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    fechaNacimiento DATE NOT NULL,
    calificacion DOUBLE NOT NULL,
    repetidor BOOLEAN NOT NULL,
    imagen VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
