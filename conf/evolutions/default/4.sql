#Tasks schema
#-!Ups

CREATE TABLE categorias(
   usuario varchar(255) NOT NULL REFERENCES usuarios (nombre),
   nombreCategoria varchar(255) NOT NULL,
   CONSTRAINT primary_key_categoria PRIMARY KEY (usuario,nombreCategoria)
);


ALTER TABLE task ADD categoria varchar(255);
ALTER TABLE task ADD CONSTRAINT categoriaDeLaTarea FOREIGN KEY (categoria) REFERENCES categorias (nombreCategoria);

INSERT INTO usuarios (nombre) VALUES ('PruebaCategoria');
INSERT INTO categorias (usuario,nombreCategoria) VALUES ('PruebaCategoria','Categoria1');
INSERT INTO task (label,usuario,categoria) VALUES ('Prueba','PruebaCategoria','Categoria1');

#-!Downs

ALTER TABLE task DROP CONSTRAINT categoriaDeLaTarea;
ALTER TABLE task DROP categorias;
DROP TABLE categorias;
