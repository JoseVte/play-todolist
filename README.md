# Play Todolist

Una APP de prueba.

## I. Funcionalidades

A continuación se van a describir todas las funcionalidades principales de la API.

### 1. Consulta de una tarea
Devuelve una tarea concreta de un usuario concreto en formato **JSON**.

* El formato de la URI para acceder a la funcionalidad es:
```
GET /{usuario}/tasks/{id}
```
* También se puede acceder al usuario `anónimo` con la siguiente URI, pero está en desuso:
```
GET /tasks/{id}
```
* Los datos devueltos están en formato **JSON**, indicando primero la `id` de la tarea, la `descripción` y la `fecha de finalización` de la misma:
``` json
{
   "id": {id},
   "label": {Descripción de la tarea},
   "fechaFin": {Fecha de finalización}
}
```
* Si no existe la tarea en el servidor o el usuario no existe devuelve un `ERROR 404` con un `HTTP`:
```
Error 404: La tarea con el identificador {id} no existe en el usuario {usuario}
Error 404: El usuario {usuario} no existe
```

### 2. Creación de nueva tarea
Crea una nueva tarea para un usuario ya existente en la base de datos.

* En la URI se debe especificar el usuario donde se desea crear la nueva tarea:
```
POST /{usuario}/tasks
```
* Si no se especifica ninguno se insertará en el usuario anónimo, pero está en desuso:
```
POST /tasks
```
* La funcionalidad devuelve **JSON**:
``` json
{
   {usuario}: {
      "id": {id},
      "label": {Descripción de la tarea},
      "fechaFin": {Fecha de finalización}
   }
}
```
* Si el usuario no existe devuelve un `ERROR 404`:
```
Error 404: El usurario {usuario} no existe
```

### 3. Listado de tareas
Lista todas las tareas de un usuario en formato **JSON**.

* En la URI se debe especificar el usuario:
```
GET /{usuario}/tasks
```
* Si no se especifica ninguno se accederá al usuario anónimo, pero está en desuso:
```
GET /tasks
```
* La funcionalidad devuelve un lista de las tareas del usuario en formato **JSON**:
``` json
{
   {usuario}: [
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización}
      },
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización}
      }
   ]
}
```
* Si no hay ninguna tarea en el usuario se devolverá una lista vacía en **JSON**:
``` json
{
   {usuario}: []
}
```
* Si el usuario no existe saltará el `ERROR 404`:
```
Error 404: El usuario {usuario} no existe
```

### 4. Borrado de una tarea
Borra una tarea de un usuario basándose en el identificador.

* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{id}
```
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
DELETE /tasks/{id}
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna tarea que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La tarea con el identificador {id} no existe para el usuario {usuario}
Error 404: El usuario {usuario} no existe
```

### 5. Borrado de varias tareas según la fecha
Borra tareas de un usuario basándose en si la fecha para finalizar ha sido vencida por la dada por parámetro.

* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{fecha}
```
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
DELETE /tasks/{fecha}
```
* La fecha debe tener el formato siguiente:
```
dd-MM-yyyy  ->  25-9-2014     
```
* Cuando se hayan borrado correctamente se mostrara el siguiente mensaje:
```
Se han borrado {numRows} de tareas del usuario {usuario} hasta la fecha {fecha}
```
* Puede devolver dos errores, dependiendo si esta mal formada la fecha o el usuario no existe:
```
Error 400: La fecha {fecha} no esta en el formato correcto
Error 404: El usuario {usuario} no existe
```

### 6. Listado de varias tareas según la fecha
Muestra todas las tareas finalizadas de un usuario hasta la fecha introducida, incluyéndola.

* El formato de la URI tiene muchas posibilidades, dependiendo de los parámetro que se inserten. Las dos primeras están en desuso:
```
GET      /tasks/finalizadas                         controllers.Application.tasksFinalizadas(usuario: String = "anonimo", fecha: String = null)
GET      /tasks/finalizadas/:fecha                  controllers.Application.tasksFinalizadas(usuario: String = "anonimo", fecha: String)
GET      /:usuario/tasks/finalizadas               controllers.Application.tasksFinalizadas(usuario: String, fecha: String = null)
GET      /:usuario/tasks/finalizadas/:fecha        controllers.Application.tasksFinalizadas(usuario: String, fecha: String)
```
* La fecha debe tener el formato siguiente:
```
dd-MM-yyyy  ->  25-9-2014     
```
* Los datos que devuelve esta en un lista en formato **JSON**:
``` json
{
   {usuario}: [
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización}
      },
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización}
      }
   ]
}
```
* Puede devolver dos errores, dependiendo si esta mal formada la fecha o el usuario no existe:
```
Error 400: La fecha {fecha} no esta en el formato correcto
Error 404: El usuario {usuario} no existe
```

## II. Links de interes

### Documentación practica 1
- [Documentación practica 1](/doc/practica1.md)

### Bitbucket
- Enlace a [Bitbucket](https://bitbucket.org/JoseVte/play-todolist)

### Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)