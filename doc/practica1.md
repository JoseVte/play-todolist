# Informe técnico de la practica 1

En este informe se van a añadir todas las funcionalidades que posee la aplicación en la practica 1

## Funcionalidades

>### 1. Consulta de una tarea
* Acceso a una tarea concreta de un usuario concreto

>>#### Implementación
>>* El método `readTask` de la clase `Task` permite acceder a una tarea en concreto con el id y el usuario. Si no existe lanza una excepción de la clase `NoSuchElementException`:
```
def read(usuario: String, id: Long): Task = DB.withConnection{ 
    implicit c =>
        SQL("select * from task where id = {id} and usuario = {usuario}")
        .on("id" -> id, "usuario" -> usuario).as(task *).head
}
```
>>* En el controlador existe el metodo `read` que recibe el id y el usuario y devuelve el JSON de la tarea en concreto:
```
def readTask(usuario: String, id: Long) = Action {
    try{
        val json = Json.toJson(Task.read(usuario,id))
        Ok(json)
    } catch {
        case e: NoSuchElementException => NotFound("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)
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
GET /{usuario}/tasks/{id}
```
>>* Tambien se puede acceder al usuario `anónimo` con la siguiente URI:
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
Error 404: La tarea con el identificador {id} no existe en el usuario {usuario}
```

>### 2. Creación de nueva tarea
* Crea una nueva tarea para un usuario ya existente

>>#### Implementación
>>* Para crear el objeto en la base de datos se utiliza el siguiente metodo de la clase `Task` que permite a partir de una descripcion y un usuario insertar dicha informacion en la base de datos, devolviendo el `id` auto-generado:
```
def create(label: String, usuario: String) : Long = {
    var id: Long = 0
    DB.withConnection{
        implicit c => 
            id = SQL("insert into task(label,usuario) values ({label},{usuario})")
            .on("label" -> label, "usuario" -> usuario).executeInsert().get
    }
    return id
}
```
>>* El método `newTask` recibe un usuario por parametro y una descripcion de una tarea por un formulario POST. Devuelve un codigo 201 con un JSON con los datos del nuevo objeto creado:
```
def newTask(usuario: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
        errors => BadRequest,
        label => {
            try{
                val id = Task.create(label,usuario)
                val json = Json.toJson(Map(usuario -> Json.toJson(new Task(id,label))))
                Created(json)
            } catch {
                case _ => NotFound("Error 404: El usuario "+usuario+" no existe")
            }
        }
    )
}
```

>>#### Ejecución
>>* En la URI se debe especificar el usuario donde se desea crear la nueva tarea:
```
POST /{usuario}/tasks
```
>>* Si no se especifica ninguno se insertará en el usuario anónimo:
```
POST /tasks
```
>>* La funcionalidad devuelve JSON:
```
{
    {usuario}: {
        "id": {id},
        "label": {Descripción de la tarea}
    }
}
```
>>* Si el usuario no existe devuelve un `error 404`:
```
Error 404: El usuario {usuario} no existe
```

>### 3. Listado de tareas
* Lista todas las tareas de un usuario en formato JSON

>>#### Implementación
>>* Para el acceso a la base de datos se utiliza un metodo de la clase `Task` que permite crear una lista de tareas a partir de un usuario:
```
def all(usuario: String): List[Task] = DB.withConnection{
    implicit c => SQL("select * from task where usuario = {usuario}").on("usuario" -> usuario).as(task *)
}
``` 
>>* Existe un método `tasks` que recibe un usuario por parámetro y devuelva la lista en JSON de las tareas de dicho usuario:
```
def tasks(usuario: String) = Action {
    val json = Json.toJson(Map(usuario -> Json.toJson(Task.all(usuario))))
    Ok(json)
}
``` 

>>#### Ejecución
>>* En la URI se debe especificar el usuario:
```
GET /{usuario}/tasks
```
>>* Si no se especifica ninguno se accedera al usuario anónimo:
```
GET /tasks
```
>>* La funcionalidad devuelve un lista de las tareas del usuario en formato JSON:
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
>>* Si no hay ninguna tarea en el usuario, o no existe dicho usuario; se devolverá una lista vacía en JSON:
```
{
    {usuario}: []
}
```

>### 4. Borrado de una tarea
* Borra una tarea basándose en el identificador

>>#### Implementación
>>* Modificada la ruta de acceso al borrado de la tarea
```
DELETE  /tasks/:id  controllers.Application.deleteTask(id: Long)
```
>>* Modificado en método `delete` de la clase Task para que devuelva un entero:
```
def delete(id: Long) : Int = {
    var result = 0;
    DB.withConnection{
        implicit c => 
            result = SQL("delete from task where id = {id}").on('id -> id).executeUpdate()
    }
    return result
}
```
>>* Se ha modificado el método `deleteTask` para que devuelva el resultado de la operación:
```
def deleteTask(id: Long) = Action {
    val resultado : Int = Task.delete(id)
    if(resultado == 1){
        Ok("Tarea "+id+" borrada correctamente")
    } else {
        NotFound("Error 404: La tarea con el identificador "+id+" no existe")
    }
}
``` 

>>#### Ejecución
>>* El formato de la URI es:
```
DELETE /tasks/{id}
```
>>* La funcionalidad devuelve un lista de tareas en formato JSON:
```
Tarea {id} borrada correctamente"
```
>>* Si no hay ninguna tarea se devolverá una lista vacía en JSON:
```
Error 404: La tarea con el identificador {id} no existe
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)