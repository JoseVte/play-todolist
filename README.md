# Play Todolist

Una app de prueba.

## Funcionalidades

### 1. Consulta de una tarea

* Acceso a una tarea concreta de un usuario concreto
* El formato de la URI para acceder a la funcionalidad es:
```
GET /{usuario}/tasks/{id}
```
* Tambien se puede acceder al usuario `anónimo` con la siguiente URI:
```
GET /tasks/{id}
```
* Los datos devueltos están en formato JSON, indicando primero la id de la tarea, la descripción y la fecha de finalización de la misma:
```
{
    "id": {id},
    "label": {Descripción de la tarea},
    "fechaFin": {Fecha de finalización}
}
```
* Si no existe la tarea el servidor devuelve un `ERROR 404`:
```
Error 404: La tarea con el identificador {id} no existe en el usuario {usuario}
```

### 2. Creación de nueva tarea

* Crea una nueva tarea para un usuario ya existente
* En la URI se debe especificar el usuario donde se desea crear la nueva tarea:
```
POST /{usuario}/tasks
```
* Si no se especifica ninguno se insertará en el usuario anónimo:
```
POST /tasks
```
* La funcionalidad devuelve JSON:
```
{
    {usuario}: {
        "id": {id},
        "label": {Descripción de la tarea},
        "fechaFin": {Fecha de finalización}
    }
}
```
* Si el usuario no existe devuelve un `error 404`:
```
Error 404: El usuario {usuario} no existe
```

### 3. Listado de tareas

* Lista todas las tareas de un usuario
* El formato de la URI es:
```
GET /{usuario}/tasks
GET /tasks      << Para el usuario anonimo
```
* La funcionalidad devuelve un lista de tareas en formato JSON:
```
{
    {usuario}: [
        {
            "id": {id},
            "label": {Descripción de la tarea},
            "fechaFin": {Fecha de finalización}
        },
        {
            "id": {id},
            "label": {Descripción de la tarea},
            "fechaFin": {Fecha de finalización}
        }
    ]
}
```
* Si no hay ninguna tarea en el usuario, o no existe dicho usuario; se devolverá una lista vacía en JSON:
```
{
    {usuario}: []
}
```

### 4. Borrado de una tarea

* Borra una tarea basándose en el identificador
* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{id}
```
* Tambien se permite borrar las tareas para el usuario anonimo
```
DELETE /tasks/{id}
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna tarea que concuerde se devolverá el `error 404`:
```
Error 404: La tarea con el identificador {id} no existe para el usuario {usuario}
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)