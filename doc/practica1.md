# Informe técnico de la practica 1

En este informe se van a añadir todas las funcionalidades que posee la aplicación en la practica 1

- [I. Funcionalidades principales](#i-funcionalidades-principales)
   - [1. Consulta de una tarea](#1-consulta-de-una-tarea)
      - [1.1 Implementación](#1-1-implementaci-n)
      - [1.2 Ejecución](#1-2-ejecuci-n)
   - [2. Creación de nueva tarea](#2-creaci-n-de-nueva-tarea)
      - [2.1 Implementación](#2-1-implementaci-n)
      - [2.2 Ejecución](#2-2-ejecuci-n)
   - [3. Listado de tareas](#3-listado-de-tareas)
      - [3.1 Implementación](#3-1-implementaci-n)
      - [3.2 Ejecución](#3-2-ejecuci-n)
   - [4. Borrado de una tarea](#4-borrado-de-una-tarea)
      - [4.1 Implementación](#4-1-implementaci-n)
      - [4.2 Ejecución](#4-2-ejecuci-n)
   - [5. Borrado de varias tareas según la fecha](#5-borrado-de-varias-tareas-seg-n-la-fecha)
      - [5.1 Implementación](#5-1-implementaci-n)
      - [5.2 Ejecución](#5-2-ejecuci-n)
   - [6. Listado de varias tareas según la fecha](#6-listado-de-varias-tareas-seg-n-la-fecha)
      - [6.1 Implementación](#6-1-implementaci-n)
      - [6.2 Ejecución](#6-2-ejecuci-n)
- [II. Funcionalidades extra](#ii-funcionalidades-extra)
   - [1. Comprobación de un usuario](#1-comprobaci-n-de-un-usuario)
   - [2. Conversión de un Task a JSON](#2-conversi-n-de-un-task-a-json)
   - [3. Mensaje HTML de error personalizado](#3-mensaje-html-de-error-personalizado)
   - [4. Expresión regular para las fechas](#4-expresi-n-regular-para-las-fechas)
- [III. Repositorios](#iii-repositorios)
   - [Bitbucket](#bitbucket)
   - [Heroku](#heroku)

## I. Funcionalidades principales

A continuación se van a describir todas las funcionalidades principales de la API. En cada funcionalidad hay una pequeña explicación del código así como la ejecución de dicha funcionalidad.

### 1. Consulta de una tarea
Devuelve una tarea concreta de un usuario concreto en formato **JSON**.

#### 1.1 Implementación
* El método `readTask` de la clase `Task` permite acceder a una tarea en concreto con el `id` y el `usuario` y devolver un `Option[Task]`:
``` scala
def read(usuario: String, id: Long): Option[Task] = DB.withConnection{ 
   implicit c =>
      SQL("select * from task where id = {id} and usuario = {usuario}")
      .on("id" -> id, "usuario" -> usuario).as(task.singleOpt)
}
```
* En el controlador existe el método `read` que recibe el `id` y el `usuario` y devuelve el **JSON** de la tarea en concreto. Se comprueba si la ha encontrado y devuelve el estado correspondiente al resultado:
``` scala
def readTask(usuario: String, id: Long) = Action {
   if(Task.comprobarUsuario(usuario)){
      Task.read(usuario,id) match {
         case Some(task) => 
            val json = Json.toJson(task)
            Ok(json)
         case None => NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
```

#### 1.2 Ejecución
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

#### 2.1 Implementación
* Para crear el objeto en la base de datos se utiliza el siguiente método de la clase `Task` que permite, a partir de una `descripción`, un `usuario` y una `fecha` (opcional), insertar dicha información en la base de datos, devolviendo el `id` auto-generado:
``` scala
def create(label: String, usuario: String,fechaFin: Option[Date]) : Long = {
   var idNuevo: Long = 0
   var aux: Date = new Date
   if(!fechaFin.isEmpty){
      aux = fechaFin.get
   }
   DB.withConnection{
      implicit c => 
         idNuevo = SQL("insert into task(label,usuario,fechaFin) values ({label},{usuario},{fechaFin})")
         .on("label" -> label, "usuario" -> usuario,"fechaFin" -> aux).executeInsert().get
   }
   return idNuevo
}
```
* El método `newTask` recibe un `usuario` por parámetro y una `descripción` de una tarea y la `fecha de finalización` por un formulario `POST`. Devuelve un código `201` con un **JSON** con los datos del nuevo objeto creado:
``` scala
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

#### 2.2 Ejecución
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
Error 404: El usuario {usuario} no existe
```

### 3. Listado de tareas
Lista todas las tareas de un usuario en formato **JSON**.

#### 3.1 Implementación
* Para el acceso a la base de datos se utiliza un método de la clase `Task` que permite crear una lista de tareas a partir de un usuario:
``` scala
def all(usuario: String): List[Task] = DB.withConnection{
   implicit c => SQL("select * from task where usuario = {usuario}").on("usuario" -> usuario).as(task *)
}
```
* Existe un método `tasks` que recibe un usuario por parámetro y devuelva la lista en **JSON** de las tareas de dicho usuario. Si el usuario no existe se devuelve un `ERROR 404`:
``` scala
def tasks(usuario: String) = Action {
   if(Task.comprobarUsuario(usuario)){
      val json = Json.toJson(Map(usuario -> Json.toJson(Task.all(usuario))))
      Ok(json)
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 3.2 Ejecución
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

#### 4.1 Implementación
* Para borrar se utiliza el método `delete` de la clase `Task` para que devuelva el número de filas que se han modificado:
``` scala
def delete(usuario:String, id: Long) : Int = {
   var numRows = 0
   DB.withConnection{
      implicit c => 
         numRows = SQL("delete from task where id = {id} and usuario = {usuario}").on("id" -> id,"usuario" -> usuario).executeUpdate()
   }
   return numRows
}
```
* El método `deleteTask` utiliza el número de filas modificado para comprobar si se ha borrado con éxito:
``` scala
def deleteTask(usuario: String, id: Long) = Action {
   if(Task.comprobarUsuario(usuario)){
      val resultado : Int = Task.delete(usuario,id)
      if(resultado == 1){
         Ok("Tarea "+id+" del usuario "+usuario+" borrada correctamente")
      } else {
         NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe para el usuario "+usuario)).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 4.2 Ejecución
* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{id}
```
* También se permite borrar las tareas para el usuario anonimo, pero está en desuso:
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
Borra tareas de un usuario basándose en si la fecha para finalizar ha sido vencida por la dada por parametro.

#### 5.1 Implementación
* Para borrar las tareas se utiliza el método `deleteDate` de la clase `Task`, que devuelva el número de tareas que se han borrado hasta la fecha:
``` scala
def deleteDate(usuario: String, fecha: Date) : Int = {
   var numRows = 0
   DB.withConnection{
      implicit c =>
         numRows = SQL("delete from task where usuario = {usuario} and fechaFin < {fecha}").on("usuario" -> usuario,"fecha" -> fecha).executeUpdate()
   }
   return numRows
}
```
* El método `deleteTaskDate` llama al método anterior para realizar el borrado. Antes se comprueba que la fecha no sea nula o este mal expresada. Aparte utiliza el número de filas modificado para enviar un mensaje al usuario:
``` scala
def deleteTaskDate(usuario: String, fecha: String) = Action {
   if(Task.comprobarUsuario(usuario)){
      val formatoURI = new SimpleDateFormat(formatoParse)
      if(fecha != null && fecha.matches(parseDate)){
         val fechaParse : Date = formatoURI.parse(fecha)
         val numRows : Int = Task.deleteDate(usuario,fechaParse)
         Ok("Se han borrado "+numRows+" de tareas del usuario "+usuario+" hasta la fecha "+fecha)
      } else {
         NotFound(errores("Error 400: La fecha ("+fecha+") no esta en el formato correcto")).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 5.2 Ejecución
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

#### 6.1 Implementación
* El método `all` de la clase `Task` tiene otra funcionalidad cuando se le añade una fecha. Esta permite mostrar solo las tareas que vayan a finalizar antes de la fecha establecida:
``` scala
def all(usuario: String, fecha: Date): List[Task] = DB.withConnection{
   implicit c => SQL("select * from task where usuario = {usuario} and fechaFin <= {fecha}").on("usuario" -> usuario,"fecha" -> fecha).as(task *)
}
```
* El método `tasksFinalizadas` comprueba la fecha, y si no se la pasa ninguna utiliza el día actual. También cabe la posibilidad que este mal escrita, entonces se devuelve un `ERROR 400`:
``` scala
def tasksFinalizadas(usuario: String, fecha: String) = Action {
   if(Task.comprobarUsuario(usuario)){
      val formatoURI = new SimpleDateFormat(formatoParse)
      var fechaParse = new Date()
      if(fecha != null && fecha.matches(parseDate)){
         fechaParse = formatoURI.parse(fecha)
         val json = Json.toJson(Task.all(usuario,fechaParse))
         Ok(json)
      }else if(fecha == null){
         fechaParse = Calendar.getInstance().getTime()
         val json = Json.toJson(Task.all(usuario,fechaParse))
         Ok(json)
      } else {
         NotFound(errores("Error 400: La fecha ("+fecha+") no esta en el formato correcto")).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 6.2 Ejecución
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

## II. Funcionalidades extra

### 1. Comprobación de un usuario
Para la comprobación de los usuarios se ha creado un método `comprobarUsuario` que permite verificar si el usuario existe:
``` scala
def comprobarUsuario(usuario: String): Boolean = {
   val aux = DB.withConnection{
      implicit c => SQL("select * from usuarios where nombre = {usuario}").on("usuario" -> usuario).as(user.*)
   }
   return !aux.isEmpty
}
```
### 2. Conversión de un Task a JSON
Método auxiliar que permite convertir un objeto de la clase `Task` en un **JSON**:
``` scala
implicit val taskWrites = new Writes[Task] {
   def writes(task: Task) = Json.obj(
      "id" -> task.id,
      "label" -> task.label,
      "fechaFin" -> task.fechaFin)
}
```
### 3. Mensaje HTML de error personalizado
Devuelve un `String` que contiene el mensaje de error junto con el código `HTTP`: 
``` scala
def errores(mensajeError: String) : String = {
   var error: String = "<html><head><title>- ooops! -</title><style>body { background:#0000aa;color:#ffffff;font-family:courier;font-size:12pt;text-align:center;margin:100px;}blink {color:yellow;}.neg {background:#fff;color:#0000aa;padding:2px 8px;font-weight:bold;}p {margin:30px 100px;text-align:left;font-size: 20;}a,a:hover {color:inherit;font:inherit;}</style></head><body><h1><span class=\"neg\">"
   error+=mensajeError
   error+="</span></h1><p><br>Usted puede esperar y ver si vuelve a estar disponible, o puede reiniciar su PC.</p><p>* Envienos un e-mail para notificar esto e intentelo mas tarde.<br />* Pulse CTRL+ALT+SUPR para reiniciar su PC. Usted perder&aacute; toda la informaci&oacute;n no guardada en cualquier programa que este ejecutando.</p>Pulse cualquier link para continuar<blink>_</blink><div class=\"menu\"><a href=\"\">Recargar</a> | <a href=\"http://www.google.es\">Google</a> |</div></body></html>";
   return error;
}
```
### 4. Expresión regular para las fechas
Constante que permite verificar el formato de una fecha introducida:
``` scala
val parseDate: String = "^([0-9]{1,2}-[0-9]{1,2}-[0-9]{4})$"
val formatoParse: String = "dd-MM-yyyy"
```

## III. Repositorios

### Bitbucket
- Enlace a [Bitbucket](https://bitbucket.org/JoseVte/play-todolist)

### Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)