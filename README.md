# Play Todolist

Una app de prueba.

## Funcionalidades

### 1. Consulta de una tarea

* Acceso a una tarea concreta
* El formato de la URI es:
```
GET /tasks/{id}
```
* Los datos devueltos estan en formato JSON:
```
{
    "id": {id},
    "label": {Descripcion de la tarea}
}
```
* Si no exite la tarea se muestra el siguiente error:
```
Error 404: La tarea con el identificador {id} no existe
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)
