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
        "label" -> task.label,
        "fechaFin" -> task.fechaFin)
}
```

>>#### Ejecución
>>* El formato de la URI para acceder a la funcionalidad es:
```
GET /{usuario}/tasks/{id}
```
>>* También se puede acceder al usuario `anónimo` con la siguiente URI:
```
GET /tasks/{id}
```
>>* Los datos devueltos están en formato JSON, indicando primero la id de la tarea, la descripción y la fecha de finalización de la misma:
```
{
    "id": {id},
    "label": {Descripción de la tarea},
    "fechaFin": {Fecha de finalización}
}
```
>>* Si no existe la tarea el servidor devuelve un `ERROR 404`:
```
Error 404: La tarea con el identificador {id} no existe en el usuario {usuario}
```

>### 2. Creación de nueva tarea
* Crea una nueva tarea para un usuario ya existente

>>#### Implementación
>>* Para crear el objeto en la base de datos se utiliza el siguiente método de la clase `Task` que permite a partir de una descripción, un usuario y una fecha (opcional) insertar dicha información en la base de datos, devolviendo el `id` auto-generado:
```
def create(label: String, usuario: String,fechaFin: Option[Date]) : Long = {
    var id: Long = 0
    var aux: Date = new Date
    if(!fechaFin.isEmpty){
        aux = fechaFin.get
    }
    DB.withConnection{
        implicit c => 
            id = SQL("insert into task(label,usuario,fechaFin) values ({label},{usuario},{fechaFin})")
            .on("label" -> label, "usuario" -> usuario,"fechaFin" -> aux).executeInsert().get
    }
    return id
}
```
>>* El método `newTask` recibe un usuario por parametro y una descripcion de una tarea y la fecha de finalización por un formulario POST. Devuelve un codigo 201 con un JSON con los datos del nuevo objeto creado:
```
def newTask(usuario: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
        errors => BadRequest,
        task => {
            try{
                val id = Task.create(task.label,usuario,task.fechaFin)
                val json = Json.toJson(Map(usuario -> Json.toJson(new Task(id,task.label,task.fechaFin))))
                Created(json)
            } catch {
                case _ : Throwable => NotFound("Error 404: El usuario "+usuario+" no existe")
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
        "label": {Descripción de la tarea},
        "fechaFin": {Fecha de finalización}
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
>>* Para el acceso a la base de datos se utiliza un método de la clase `Task` que permite crear una lista de tareas a partir de un usuario:
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
>>* Si no hay ninguna tarea en el usuario, o no existe dicho usuario; se devolverá una lista vacía en JSON:
```
{
    {usuario}: []
}
```

>### 4. Borrado de una tarea
* Borra una tarea de un usuario basándose en el identificador

>>#### Implementación
>>* Para borrar se utiliza el método `delete` de la clase `Task` para que devuelva el numero de filas que se han modificado:
```
def delete(usuario:String, id: Long) : Int = {
    var numRows = 0
    DB.withConnection{
        implicit c => 
            numRows = SQL("delete from task where id = {id} and usuario = {usuario}").on("id" -> id,"usuario" -> usuario).executeUpdate()
    }
    return numRows
}
```
>>* El método `deleteTask` utiliza el numero de filas modificado para comprobar si se ha borrado con exito:
```
def deleteTask(usuario: String, id: Long) = Action {
        val resultado : Int = Task.delete(usuario,id)
        if(resultado == 1){
      Ok("Tarea "+id+" borrada correctamente")
    } else {
      NotFound("Error 404: La tarea con el identificador "+id+" no existe para el usuario "+usuario)
    }
}
``` 

>>#### Ejecución
>>* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{id}
```
>>* También se permite borrar las tareas para el usuario anonimo
```
DELETE /tasks/{id}
```
>>* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
>>* Si no hay ninguna tarea que concuerde se devolverá el `error 404`:
```
Error 404: La tarea con el identificador {id} no existe para el usuario {usuario}
```

>### 5. Borrado de varias tareas segun la fecha
* Borra una o varias tareas de un usuario basándose en la fecha para finalizar

>>#### Implementación
>>* Para borrar ñas tareas se utiliza el método `deleteDate` de la clase `Task`, que devuelva el numero de filas que se han modificado:
```
def deleteDate(usuario: String, fecha: Date) : Int = {
    var numRows = 0
    DB.withConnection{
        implicit c =>
            numRows = SQL("delete from task where usuario = {usuario} and fechaFin < {fecha}").on("usuario" -> usuario,"fecha" -> fecha).executeUpdate()
    }
    return numRows
}
```
>>* El método `deleteTaskDate` utiliza el numero de filas modificado para enviar un mensaje al usuario:
```
def deleteTaskDate(usuario: String, fecha: String) = Action {
    val formatoURI = new SimpleDateFormat("dd-MM-yyyy")
    val fechaParse : Date = formatoURI.parse(fecha)
    val numRows : Int = Task.deleteDate(usuario,fechaParse)
    Ok("Se han borrado "+numRows+" de tareas del usuario "+usuario+" hasta la fecha "+fecha)
}
``` 

>>#### Ejecución
>>* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{fecha}
```
>>* También se permite borrar las tareas para el usuario anonimo
```
DELETE /tasks/{fecha}
```
>>* La fecha debe tener el formato siguiente:
```
dd-MM-yyyy  ->  25-9-2014     
```
>>* Cuando se hayan borrado correctamente se mostrara el siguiente mensaje:
```
Se han borrado {numRows} de tareas del usuario {usuario} hasta la fecha {fecha}
```

> #### Enlace a la app en Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)