#Tasks schema
#-!Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE usuarios(
   id integer NOT NULL DEFAULT nextval('user_id_seq'),
   nombre varchar(255) UNIQUE NOT NULL
);

ALTER TABLE task ADD usuario varchar(255) NOT NULL;
ALTER TABLE task ADD CONSTRAINT usuarioDeLaTarea FOREIGN KEY (usuario) REFERENCES usuarios (nombre);

INSERT INTO usuarios (nombre) VALUES ('anonimo');
INSERT INTO usuarios (nombre) VALUES ('Prueba');
INSERT INTO task (label,usuario) VALUES ('Prueba','anonimo');
INSERT INTO task (label,usuario)VALUES ('Prueba *','Prueba');

#-!Downs

ALTER TABLE task DROP CONSTRAINT usuarioDeLaTarea;
ALTER TABLE task DROP usuario;

DROP TABLE usuarios;
DROP SEQUENCE user_id_seq;