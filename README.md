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

* Lista todas las tareas
* El formato de la URI es:
```
GET /tasks
```
* La funcionalidad devuelve un lista de tareas en formato JSON:
```
[
   {
      "id": {id},
      "label": {Descripción de la tarea}
   },
   {
      "id": {id},
      "label": {Descripción de la tarea}
   }
]
```
* Si no hay ninguna tarea se devolverá una lista vacía en JSON:
```
[]
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)