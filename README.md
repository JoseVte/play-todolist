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

* Crea una nueva tarea
* El formato de la URI es:
```
POST /tasks
```
* La funcionalidad devuelve la descripción de la tarea si se ha podido crear en formato JSON:
```
{Descripción de la tarea}
```
* Si por algún error no se puede crear la tarea se muestra el siguiente error:
```
Error 500: No se ha podido crear la nueva tarea
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