#Tasks schema
#-!Ups

ALTER TABLE task ADD fechaFin DATE;

INSERT INTO usuarios (nombre) VALUES ('PruebaTiempo');
INSERT INTO task (label,usuario,fechaFin) VALUES ('Prueba con fecha','PruebaTiempo',PARSEDATETIME('27/12/2014','dd/MM/yyyy'));

#-!Downs

ALTER TABLE task DROP fechaFin;