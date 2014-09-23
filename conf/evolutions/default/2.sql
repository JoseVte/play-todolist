#Tasks schema
#-!Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE usuarios(
   id integer NOT NULL DEFAULT nextval('task_id_seq'),
   nombre varchar(255) UNIQUE NOT NULL
);

ALTER TABLE task ADD usuario varchar(255) NOT NULL;
ALTER TABLE task ADD CONSTRAINT usuarioDeLaTarea FOREIGN KEY (usuario) REFERENCES usuarios (nombre);

INSERT INTO usuarios (id,nombre) VALUES (1,'anonimo');
INSERT INTO usuarios (id,nombre) VALUES (2,'Prueba');
INSERT INTO task (id,label,usuario) VALUES (1,'Prueba','anonimo');
INSERT INTO task (id,label,usuario)VALUES (2,'Prueba *','Prueba');

#-!Downs

ALTER TABLE task DROP CONSTRAINT usuarioDeLaTarea;
ALTER TABLE task DROP usuario;

DROP TABLE usuarios;
DROP SEQUENCE user_id_seq;