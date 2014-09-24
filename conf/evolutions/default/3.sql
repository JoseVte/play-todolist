#Tasks schema
#-!Ups

ALTER TABLE task ADD fechaFin DATE;

INSERT INTO usuarios (id,nombre) VALUES (3,'PruebaTiempo');
INSERT INTO task (id,label,usuario,fechaFin) VALUES (3,'Prueba con fecha','PruebaTiempo',PARSEDATETIME('27/12/2014','dd/mm/yyyy'));

#-!Downs

ALTER TABLE task DROP fechaFin;