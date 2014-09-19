# Informe técnico de la practica 1

En este informe se van a añadir todas las funcionalidades que posee la aplicación en la practica 1

## Funcionalidades

>### 1. Consulta de una tarea
* Acceso a una tarea concreta

>>#### Implementación
>>* Se ha modificado el archivo de rutas agregando la nueva funcionalidad:
```
GET /tasks/:id  controllers.Application.readTask(id: Long)
```
>>* Para ello se ha debido de añadir un nuevo método en la clase Task el cual permite obtener solo una tarea identificada por el id:
```
def read(id:Long): Task = DB.withConnection{ 
    implicit c =>
        SQL("select * from task where id = {id}").on("id" -> id).as(task *).head
}
```
>>* También se ha agregado un método en el controlador para que se pueda utilizar la funcionalidad:
```
def readTask(id: Long) = Action {
        try{
            val json = Json.toJson(Task.read(id))
            Ok(json)
        } catch {
            case _ => NotFound("Error 404: La tarea con el identificador "+id+" no existe")
        }
}
```
>>* Por último, se ha añadido la conversión del objeto Task a JSON de forma rápida:
```
implicit val taskWrites = new Writes[Task] {
    def writes(task: Task) = Json.obj(
        "id" -> task.id,
        "label" -> task.label)
}
```

>>#### Ejecución
>>* El formato de la URI para acceder a la funcionalidad es:
```
GET /tasks/{id}
```
>>* Los datos devueltos están en formato JSON, indicando primero la id de la tarea y luego la descripción de la misma:
```
{
    "id": {id},
    "label": {Descripción de la tarea}
}
```
>>* Si no existe la tarea el servidor devuelve un `ERROR 404`:
```
Error 404: La tarea con el identificador {id} no existe
```

>### 2. Creación de nueva tarea
* Crea una nueva tarea

>>#### Implementación
>>* Se ha modificado el método `newTask` para que devuelva el JSON o el error:
```
def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
        errors => BadRequest("Error 500: No se ha podido crear la nueva tarea"),
        label => {
            Task.create(label)
            val json = Json.toJson(label)
            Created(json)
        }
    )
}
```

>>#### Ejecución
>>* El formato de la URI es:
```
POST /tasks
```
>>* La funcionalidad devuelve la descripción de la tarea si se ha podido crear en formato JSON:
```
{Descripción de la tarea}
```
>>* Si por algún error no se puede crear la tarea se muestra el siguiente error:
```
Error 500: No se ha podido crear la nueva tarea
```

>### 3. Listado de tareas
* Lista todas las tareas en formato JSON

>>#### Implementación
>>* Se ha modificado el método `tasks` para que devuelva la lista en JSON:
```
def tasks = Action {
    Ok(Json.toJson(Task.all()))
}
``` 

>>#### Ejecución
>>* El formato de la URI es:
```
GET /tasks
```
>>* La funcionalidad devuelve un lista de tareas en formato JSON:
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