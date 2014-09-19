# Informe tecnico de la practica 1

En este informe se van a añadir todas las funcionalidades que posee la aplicacion en la practica 1

## Funcionalidades

>### 1. Consulta de una tarea
* Acceso a una tarea concreta

>>#### Implementación
>>* Se ha modificado el archivo de rutas agregando la nueva funcionalidad:
```
GET /tasks/:id  controllers.Application.readTask(id: Long)
```
>>* Para ello se ha debido de añadir un nuevo metodo en la clase Task el cual permite obtener solo una tarea identificada por el id:
```
def read(id:Long): Task = DB.withConnection{ 
    implicit c =>
        SQL("select * from task where id = {id}").on("id" -> id).as(task *).head
}
```
>>* Tambien se ha agregado un metodo en el controlador para que se pueda utilizar la funcionalidad:
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
>>* Por ultimo, se ha añadido la conversion del objeto Task a JSON de forma rapida:
```
implicit val taskWrites = new Writes[Task] {
    def writes(task: Task) = Json.obj(
        "id" -> task.id,
        "label" -> task.label)
}
```

>>#### Ejecución
>>* El formato de la URI para acceder a la funcionildad es:
```
GET /tasks/{id}
```
>>* Los datos devueltos estan en formato JSON, indicando primero la id de la tarea y luego la descripcion de la misma:
```
{
    "id": {id},
    "label": {Descripcion de la tarea}
}
```
>>* Si no exite la tarea el servidor devuelve un `ERROR 404` indicando que la tarea no existe:
```
Error 404: La tarea con el identificador {id} no existe
```
