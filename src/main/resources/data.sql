DELETE
FROM Estudiante;

-- Asignar valores a las columnas de las tablas autonumericos para reiniciar el contador

ALTER TABLE Estudiante
    ALTER COLUMN id RESTART WITH 1;

-- Datos de prueba
INSERT INTO Estudiante (nia, curso, apellidos, nombre, email, fechaNacimiento, calificacion, repetidor, imagen, created_at, updated_at) VALUES
('20230001', '1ºDAW', 'García Pérez', 'Juan', 'juan@correo.com', '1990-01-01', 7.5, FALSE, '1682763355431.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500'),
('20230002', '1ºDAW', 'López Gómez', 'María', 'maria@correo.com', '1991-02-02', 8.5, FALSE, '1682763355432.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500'),
('20230003', '2ºDAW', 'González Álvarez', 'Luis', 'luis@correo.com', '1990-03-03', 9.5, TRUE, '1682763355433.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500'),
('20230004', '1ºDAW', 'Rodríguez Fernández', 'Ana', 'ana@correo.com', '1992-04-04', 5.5, FALSE, '1682763355434.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500'),
('20230005', '2ºDAW', 'Martínez Sánchez', 'José', 'jose@correo.com', '1991-05-05', 2.5, TRUE, '1682763355435.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500'),
('20230006', '2ºDAW', 'Sánchez Díaz', 'Pedro', 'pedro@correo.com', '1993-06-06', 6.5, FALSE, '1682763355436.png', '2024-04-26T18:54:47.097563500', '2024-04-26T18:54:47.097563500');
