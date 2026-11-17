

SET NAMES utf8mb4;
USE gestion_proyectos;


CREATE TABLE IF NOT EXISTS proveedores (
    codigo    VARCHAR(10)  NOT NULL,
    nombre    VARCHAR(50)  NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS piezas (
    precio      DECIMAL(10, 2) NOT NULL,
    codigo      VARCHAR(10)    NOT NULL,
    nombre      VARCHAR(50)    NOT NULL,
    descripcion VARCHAR(255)   DEFAULT NULL,
    PRIMARY KEY (codigo)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS proyectos (
    codigo VARCHAR(10) NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS gestiones (
    cantidad         INT         NOT NULL,
    fecha            DATE        NOT NULL,
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    codigo_pieza     VARCHAR(10) NOT NULL,
    codigo_proveedor VARCHAR(10) NOT NULL,
    codigo_proyecto  VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK92hvb98bhd53rs4gqfxyv3maf FOREIGN KEY (codigo_pieza) REFERENCES piezas (codigo),
    CONSTRAINT FKidow3otkgn6hsqyijhld57f6v FOREIGN KEY (codigo_proveedor) REFERENCES proveedores (codigo),
    CONSTRAINT FK8p861jxf1tpjdo4o5xvp5cks8 FOREIGN KEY (codigo_proyecto) REFERENCES proyectos (codigo)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS usuarios (
    fechaAlta DATETIME(6)  NOT NULL,
    nombre    VARCHAR(30)  NOT NULL,
    sal       VARCHAR(50)  NOT NULL,
    hash      VARCHAR(100) NOT NULL,
    PRIMARY KEY (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS registro_accesos (
    exito     BIT(1)      NOT NULL,
    fechaHora DATETIME(6) NOT NULL,
    id        BIGINT      NOT NULL AUTO_INCREMENT,
    usuario   VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS registro_operaciones (
    fechaHora     DATETIME(6)  NOT NULL,
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    operacion     VARCHAR(15)  NOT NULL,
    entidad       VARCHAR(30)  NOT NULL,
    identificador VARCHAR(30)  NOT NULL,
    usuario       VARCHAR(30)  NOT NULL,
    detalle       VARCHAR(500) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Usuarios (contraseñas hasheadas con PBKDF2, igual que la app)
--   admin / admin1234
--   chej  / chej1234
--   oier  / oier1234

INSERT INTO usuarios (nombre, hash, sal, fechaAlta) VALUES
('admin', 'AMEUfZgmSu+rfkAhNlrx0AzWt3iI8Do1eXlVe/bXgCw=', '0rgXltpgB6/ZfZWfsCoepA==', '2026-11-01 09:00:00'),
('chej',  'hvbgUJqsgjtCJQUNbjfg9wiMu0DGtsNe4u1v3aysAD8=', 'dod6vr6pq2kMyuQpOB2i0w==', '2026-11-01 09:00:00'),
('oier',  '6xx/igsAl8AoVubFa7e4NkPT9C74lgOLzVD4oNMORio=', 'KYxtrbA4V3U3lwjY4FpGHA==', '2026-11-01 09:00:00');


INSERT INTO proveedores (codigo, nombre, apellidos, direccion) VALUES
('P01', 'Ana',    'García López',      'Calle Dato 12, Vitoria-Gasteiz'),
('P02', 'Iñaki',  'Etxeberria Ruiz',   'Avenida Gasteiz 45, Vitoria-Gasteiz'),
('P03', 'Lucía',  'Martínez Sanz',     'Gran Vía 30, Bilbao'),
('P04', 'Jon',    'Agirre Fernández',  'Calle Mayor 8, Donostia'),
('P05', 'Marta',  'Pérez Ibáñez',      'Polígono Júndiz, Parcela 14, Vitoria-Gasteiz'),
('P06', 'Carlos', 'Ortega Díaz',       'Calle Estafeta 3, Pamplona');


INSERT INTO piezas (codigo, nombre, precio, descripcion) VALUES
('PZ01', 'Tornillo M8',         0.35, 'Tornillo hexagonal de acero galvanizado, 40 mm'),
('PZ02', 'Tuerca M8',           0.15, 'Tuerca hexagonal de acero galvanizado'),
('PZ03', 'Viga IPE 200',      185.00, 'Viga de acero laminado, 6 metros'),
('PZ04', 'Saco de cemento',     6.90, 'Cemento Portland, saco de 25 kg'),
('PZ05', 'Ladrillo hueco',      0.42, 'Ladrillo cerámico hueco doble'),
('PZ06', 'Panel solar 450 W',  210.00, 'Panel fotovoltaico monocristalino'),
('PZ07', 'Cable 6 mm²',         1.85, 'Cable de cobre unipolar, precio por metro'),
('PZ08', 'Bisagra industrial',  12.50, 'Bisagra de acero inoxidable para puertas pesadas');


INSERT INTO proyectos (codigo, nombre, ciudad) VALUES
('PR01', 'Pasarela del Zadorra',      'Vitoria-Gasteiz'),
('PR02', 'Polideportivo Mendizorroza', 'Vitoria-Gasteiz'),
('PR03', 'Parque solar Arasur',        'Rivabellosa'),
('PR04', 'Rehabilitación Casco Viejo', 'Bilbao'),
('PR05', 'Ampliación del tranvía',     'Vitoria-Gasteiz');


INSERT INTO gestiones (fecha, cantidad, codigo_proveedor, codigo_pieza, codigo_proyecto) VALUES
('2025-02-10',  500, 'P01', 'PZ01', 'PR01'),
('2025-02-10',  500, 'P01', 'PZ02', 'PR01'),
('2025-03-05',   12, 'P03', 'PZ03', 'PR01'),
('2025-04-22',  300, 'P05', 'PZ04', 'PR02'),
('2025-05-14', 8000, 'P05', 'PZ05', 'PR02'),
('2025-06-03',   20, 'P03', 'PZ03', 'PR02'),
('2025-07-18', 1200, 'P01', 'PZ01', 'PR02'),
('2025-09-09',  150, 'P02', 'PZ06', 'PR03'),
('2025-09-30', 2500, 'P02', 'PZ07', 'PR03'),
('2025-11-12',  200, 'P04', 'PZ06', 'PR03'),
('2026-01-15', 2000, 'P01', 'PZ01', 'PR03'),
('2026-01-15', 2000, 'P01', 'PZ02', 'PR03'),
('2026-02-20',  180, 'P05', 'PZ04', 'PR04'),
('2026-03-11', 4500, 'P04', 'PZ05', 'PR04'),
('2026-04-07',    6, 'P03', 'PZ03', 'PR04'),
('2026-05-19',  800, 'P02', 'PZ07', 'PR04'),
('2026-06-25',  100, 'P02', 'PZ06', 'PR03'),
('2026-08-04',  250, 'P05', 'PZ04', 'PR01'),
('2026-09-16',  600, 'P01', 'PZ02', 'PR04'),
('2026-10-08',   80, 'P04', 'PZ06', 'PR02'),
('2026-10-29', 1500, 'P04', 'PZ07', 'PR02'),
('2026-11-10',   10, 'P03', 'PZ03', 'PR03');
