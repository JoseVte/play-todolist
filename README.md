# Play Todolist

Una app de prueba.

## Funcionalidades

### 1. Consulta de una tarea

* Acceso a una tarea concreta
* El formato de la URI es:
```
GET /tasks/{id}
```
* Los datos devueltos están en formato JSON:
```
{
    "id": {id},
    "label": {Descripción de la tarea}
}
```
* Si no existe la tarea se muestra el siguiente error:
```
Error 404: La tarea con el identificador {id} no existe
```

### 2. Creación de nueva tarea

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
        "label": {Descripción de la tarea}
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
            "label": {Descripción de la tarea}
        },
        {
            "id": {id},
            "label": {Descripción de la tarea}
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
* El formato de la URI es:
```
DELETE /tasks/{id}
```
* La funcionalidad devuelve un lista de tareas en formato JSON:
```
Tarea {id} borrada correctamente"
```
* Si no hay ninguna tarea se devolverá una lista vacía en JSON:
```
Error 404: La tarea con el identificador {id} no existe
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)