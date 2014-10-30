# Informe técnico de la practica 2

En este informe se van a describir todas las funcionalidades nuevas y los tests para las funcionalidades antiguas.
[Aqui](/doc/practica1.md) se encuentra la documentación referentes a las funcionalidades antiguas.

<!-- MarkdownTOC -->

- [I. Funcionalidades principales](#i-funcionalidades-principales)
   - [1. Crear categoria](#1-crear-categoria)
      - [1.1 Implementación](#11-implementación)
      - [1.2 Ejecución](#12-ejecución)
   - [2. Mostrar todas las categorias](#2-mostrar-todas-las-categorias)
      - [2.1 Implementación](#21-implementación)
      - [2.2 Ejecución](#22-ejecución)
   - [3. Modificar el nombre de la categoria](#3-modificar-el-nombre-de-la-categoria)
      - [3.1 Implementación](#31-implementación)
      - [3.2 Ejecución](#32-ejecución)
   - [4. Borrar la categoria](#4-borrar-la-categoria)
      - [4.1 Implementación](#41-implementación)
      - [4.2 Ejecución](#42-ejecución)
   - [5. Añadir tareas a categoria](#5-añadir-tareas-a-categoria)
      - [5.1 Implementación](#51-implementación)
      - [5.2 Ejecución](#52-ejecución)
   - [6. Listado de tareas](#6-listado-de-tareas)
      - [6.1 Implementación](#61-implementación)
      - [6.2 Ejecución](#62-ejecución)
   - [7. Quitar todas las tareas de una categoria](#7-quitar-todas-las-tareas-de-una-categoria)
      - [7.1 Implementación](#71-implementación)
      - [7.2 Ejecución](#72-ejecución)
   - [8. Modificar la categoria de una tarea](#8-modificar-la-categoria-de-una-tarea)
      - [8.1 Implementación](#81-implementación)
      - [8.2 Ejecución](#82-ejecución)
   - [9. Borrar la categoria de una tarea](#9-borrar-la-categoria-de-una-tarea)
      - [9.1 Implementación](#91-implementación)
      - [9.2 Ejecución](#92-ejecución)
- [II. Test añadidos](#ii-test-añadidos)
   - [1. Test Feature 1](#1-test-feature-1)
      - [1.1 Test en el modelo](#11-test-en-el-modelo)
         - [1.1.1 Crear una tarea](#111-crear-una-tarea)
         - [1.1.2 Todas las tareas](#112-todas-las-tareas)
         - [1.1.3 Una tarea concreta](#113-una-tarea-concreta)
         - [1.1.4 Borrado de una tarea](#114-borrado-de-una-tarea)
      - [1.2 Test en el controlador](#12-test-en-el-controlador)
         - [1.2.1 Ruta incorrecta y ruta por defecto](#121-ruta-incorrecta-y-ruta-por-defecto)
         - [1.2.2 Todas las tareas](#122-todas-las-tareas)
         - [1.2.3 Una tarea concreta](#123-una-tarea-concreta)
         - [1.2.4 Crear una tarea](#124-crear-una-tarea)
         - [1.2.5 Borrado de una tarea](#125-borrado-de-una-tarea)
   - [2. Test Feature 2](#2-test-feature-2)
      - [2.1 Test en el modelo](#21-test-en-el-modelo)
         - [2.1.1 Crear un usuario](#211-crear-un-usuario)
         - [2.1.2 Leer un usuario](#212-leer-un-usuario)
         - [2.1.3 Todos los usuarios](#213-todos-los-usuarios)
         - [2.1.4 Modificar un usuario](#214-modificar-un-usuario)
         - [2.1.5 Borrar un usuario](#215-borrar-un-usuario)
      - [2.2 Test en el controlador](#22-test-en-el-controlador)
         - [2.2.1 Todos las tareas de un usuario](#221-todos-las-tareas-de-un-usuario)
         - [2.2.2 Crear una tarea para un usuario](#222-crear-una-tarea-para-un-usuario)
         - [2.2.3 Una tarea de un usuario](#223-una-tarea-de-un-usuario)
         - [2.2.4 Borrado de una tarea de un usuario](#224-borrado-de-una-tarea-de-un-usuario)
   - [3. Test Feature 3](#3-test-feature-3)
      - [3.1 Test en el modelo](#31-test-en-el-modelo)
         - [3.1.1 Crear una tarea con fecha](#311-crear-una-tarea-con-fecha)
         - [3.1.2 Borrar tareas por fecha](#312-borrar-tareas-por-fecha)
         - [3.1.3 Mostrar tareas por fecha](#313-mostrar-tareas-por-fecha)
      - [3.2 Test en el controlador](#32-test-en-el-controlador)
         - [3.2.1 Crear una tarea con fecha](#321-crear-una-tarea-con-fecha)
         - [3.2.2 Borrado de tareas por fecha](#322-borrado-de-tareas-por-fecha)
         - [3.2.3 Mostrar tareas por fecha](#323-mostrar-tareas-por-fecha)
- [III. Repositorios](#iii-repositorios)
   - [Bitbucket](#bitbucket)
   - [Heroku](#heroku)

<!-- /MarkdownTOC -->

## I. Funcionalidades principales

### 1. Crear categoria

#### 1.1 Implementación

* El método `create` de la clase `Categoria` permite crear una categoria nueva a partir de un `usuario` y un `nombre`:
``` scala
def create(usuario: String, nombreCategoria: String): Int = DB.withConnection {
   implicit c => SQL("INSERT INTO categorias (usuario,nombreCategoria) VALUES ({usuario},{nombreCategoria})")
      .on("usuario" -> usuario,"nombreCategoria" -> nombreCategoria).executeUpdate()
}
```
* En el controlador existe el método `newCategoria` que recibe el `usuario` y devuelve el **JSON** de la categoria creada. Se comprueba si el usuario existe o si la categoria ya existe:
``` scala
def newCategoria(usuario: String) = Action { implicit request =>
   categoriaForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      categoria => {
         if(User.comprobarUsuario(usuario)){
            if(!Categoria.comprobarCategoria(usuario,categoria)){
               Categoria.create(usuario,categoria)
               val json = Json.toJson(Map(usuario -> Json.toJson(new Categoria(usuario,categoria))))
               Created(json)
            } else {
               BadRequest(errores("Error 400: La categoria "+categoria+" ya existe")).as("text/html")
            }
         } else {
            NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
         }
      }
   )
}
```

#### 1.2 Ejecución

* El formato de la URI para acceder a la funcionalidad es:
```
POST /{usuario}/categorias
```
* También se puede acceder al usuario `anónimo` con la siguiente URI, pero está en desuso:
```
POST /categorias
```
* Los datos devueltos están en formato **JSON**, indicando primero la `id` de la tarea, la `descripción` y la `fecha de finalización` de la misma:
``` json
{
   {usuario}: {
      "nombreCategoria": {nombre categoria}
   }
}
```
* Si el usuario no existe devuelve un `ERROR 404`:
```
Error 404: El usuario {usuario} no existe
```
* Si la categoria ya existe devuelve un `ERROR 400`:
```
Error 400: La categoria {categoria} ya existe
```

### 2. Mostrar todas las categorias

#### 2.1 Implementación

* El método `all` de la clase `Categoria` permite acceder a todas las categorias del `usuario` y devolver un `Option[Task]`:
``` scala
def all(usuario: String): List[Categoria] = DB.withConnection {
   implicit c => SQL("SELECT * FROM categorias WHERE usuario = {usuario}")
      .on("usuario" -> usuario).as(categoria *)
}
```
* En el controlador existe el método `categorias` que recibe el `usuario` y devuelve el **JSON** de con todas las categorias de ese usuario:
``` scala
def categorias(usuario: String) = Action {
   if(User.comprobarUsuario(usuario)){
      val json = Json.toJson(Map(usuario -> Json.toJson(Categoria.all(usuario))))
      Ok(json)
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
```

#### 2.2 Ejecución

* El formato de la URI para acceder a la funcionalidad es:
```
GET /{usuario}/categorias
```
* También se puede acceder al usuario `anónimo` con la siguiente URI, pero está en desuso:
```
GET /categorias
```
* Los datos devueltos están en formato **JSON**, el nombre de la categoria:
``` json
{
   {usuario}: [
      {
         "nombreCategoria": {nombre categoria}
      },
      {
         "nombreCategoria": {nombre categoria}
      }
   ]
}
```
* Si el usuario no existe devuelve un `ERROR 404` con un `HTTP`:
```
Error 404: El usuario {usuario} no existe
```

### 3. Modificar el nombre de la categoria

#### 3.1 Implementación

* Para modificar se utiliza el método `update` de la clase `Categoria` para que devuelva un **Boolean** indicando si se ha podido modificar:
``` scala
def update(usuario: String, nombreAnt: String, nombreNuevo: String): Boolean = (1 == DB.withConnection {
   implicit c => SQL("UPDATE categorias c SET c.nombreCategoria = {nombreNuevo} WHERE c.nombreCategoria = {nombreAnt} AND c.usuario = {usuario}")
      .on("usuario" -> usuario,"nombreAnt" -> nombreAnt,"nombreNuevo" -> nombreNuevo).executeUpdate()
})
```
* El método `updateCategoria` comprueba el `formulario`, el `usuario`, y la `categoria` antes de modificarla:
``` scala
def updateCategoria(usuario: String) = Action { implicit request =>
   categoriaUpdateForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      form => {
         if(User.comprobarUsuario(usuario)){
            if(!Categoria.comprobarCategoria(usuario,form._2)){
               if(Categoria.update(usuario,form._1,form._2)){
                  Ok("La categoria "+form._1+" se ha modificado correctamente a "+form._2)
               } else {
                  NotFound(errores("Error 404: La categoria "+form._1+" no se ha podido modificar porque no se encuentra")).as("text/html")
               }
            } else {
               BadRequest(errores("Error 400: La categoria "+form._1+" no se ha podido modificar porque el nuevo nombre ya existe")).as("text/html")
            }
         } else {
            NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
         }
      }
   )
}
``` 

#### 3.2 Ejecución
* El formato de la URI para borrar es:
```
POST /{usuario}/categorias/update
```
* También se permite borrar las tareas para el usuario anonimo, pero está en desuso:
```
POST /categorias/update
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna categoria que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no se ha podido modificar porque no se encuentra
Error 404: El usuario {usuario} no existe
```
* Tambien puede fallar si ya existe la categoria:
```
"Error 400: La categoria {categoria} no se ha podido modificar porque el nuevo nombre ya existe"
```

### 4. Borrar la categoria

#### 4.1 Implementación

* Para borrar se utiliza el método `delete` de la clase `Categoria` para que devuelva un **Boolean** si se ha podido borrar:
``` scala
def delete(usuario: String, nombre: String): Boolean = (1 == DB.withConnection {
   implicit c => SQL("DELETE FROM categorias WHERE usuario = {usuario} AND nombreCategoria = {nombre}")
      .on("usuario" -> usuario,"nombre" -> nombre).executeUpdate()
})
```
* El método `deleteCategoria` utiliza el número de filas modificado para comprobar si se ha borrado con éxito:
``` scala
def deleteCategoria(usuario: String, categoria: String) = Action {
   if(User.comprobarUsuario(usuario)){
      if(Categoria.delete(usuario,categoria)){
         Ok("La categoria "+categoria+" se ha borrado correctamente")
      } else {
         NotFound(errores("Error 404: La categoria "+categoria+" no existe")).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 4.2 Ejecución

* El formato de la URI para borrar es:
```
DELETE /{usuario}/categorias/{categoria}
```
* También se permite borrar las tareas para el usuario anonimo, pero está en desuso:
```
DELETE /categorias/{categoria}
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna categoria que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 5. Añadir tareas a categoria

#### 5.1 Implementación

* Para crear una `tarea` que este dentro de una `categoria` se utiliza el metodo `create` de la clase `Task`:
```
def create(label: String, usuario: String, categoria: String,fechaFin: Option[Date]) : Long = {
   var idNuevo: Long = 0
   var aux: Date = new Date
   if(fechaFin!=null && !fechaFin.isEmpty){
      aux = fechaFin.get
   }
   DB.withConnection{
      implicit c => 
         idNuevo = SQL("insert into task(label,usuario,fechaFin, categoria) values ({label},{usuario},{fechaFin},{categoria})")
         .on("label" -> label, "usuario" -> usuario, "categoria" -> categoria, "fechaFin" -> aux).executeInsert().get
   }
   return idNuevo
}
```
* El método `newTask` recibe un `usuario` por parámetro y una `descripción` de una tarea, una `categoria` y la `fecha de finalización` por un formulario `POST`. Devuelve un código `201` con un **JSON** con los datos del nuevo objeto creado:
```
def newTask(usuario: String) = Action { implicit request =>
   taskForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      task => {
         if(User.comprobarUsuario(usuario)){
            var auxCategoria: String = null
            if(task.categoria!=None && !task.categoria.isEmpty){
               auxCategoria=task.categoria.get
            }
            val id = Task.create(task.label,usuario,auxCategoria,task.fechaFin)
            val json = Json.toJson(Map(usuario -> Json.toJson(Task.read(usuario,id))))
            Created(json)
         } else {
            NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
         }
      }
   )
}
```

#### 5.2 Ejecución

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
      "fechaFin": {Fecha de finalización},
      "categoria": {Nombre de la categoria}
   }
}
```
* Si el usuario no existe devuelve un `ERROR 404`:
```
Error 404: El usuario {usuario} no existe
```

### 6. Listado de tareas

#### 6.1 Implementación
* Para el acceso a la base de datos se utiliza un método de la clase `Task` que permite crear una lista de tareas a partir de un usuario y una categoria:
``` scala
def all(usuario: String, categoria: String): List[Task] = DB.withConnection {
   implicit c => SQL("select * from task where usuario = {usuario} and categoria = {categoria}")
   .on("usuario" -> usuario, "categoria" -> categoria).as(task *)
}
```
* Existe un método `tasksPorCategoria` que recibe un usuario y una categoria por parámetro y devuelva la lista en **JSON** de las tareas de dicho usuario. Si el usuario o la categoria no existen se devuelve un `ERROR 404`:
``` scala
def tasksPorCategoria(usuario: String, categoria: String) = Action{
   if(User.comprobarUsuario(usuario)){
      if(Categoria.comprobarCategoria(usuario,categoria)){
         Ok(Json.toJson(Task.all(usuario,categoria)))
      } else {
         NotFound(errores("Error 404: La categoria "+categoria+" no existe")).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 6.2 Ejecución
* En la URI se debe especificar el usuario:
```
GET /{usuario}/categorias/{categoria}/tasks
```
* Si no se especifica ninguno se accederá al usuario anónimo, pero está en desuso:
```
GET /categorias/{categoria}/tasks
```
* La funcionalidad devuelve un lista de las tareas del usuario en formato **JSON**:
``` json
{
   {usuario}: [
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización},
         "categoria": {Nombre de la categoria}
      },
      {
         "id": {id},
         "label": {Descripción de la tarea}
         "fechaFin": {Fecha de finalización},
         "categoria": {Nombre de la categoria}
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
* Si no hay ninguna categoria que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 7. Quitar todas las tareas de una categoria

#### 7.1 Implementación
* Para borrar las tareas se utiliza el método `deleteCategoria` de la clase `Task`, que devuelva el número de tareas que se han borrado hasta en la categoria:
``` scala
def deleteCategoria(usuario: String, categoria: String) : Int = DB.withConnection{
   implicit c => SQL("delete from task where usuario = {usuario} and categoria = {categoria}")
      .on("usuario" -> usuario, "categoria" -> categoria).executeUpdate()
}
```
* El método `deleteTaskCategoria` llama al método anterior para realizar el borrado. Antes se comprueba que la categoria exista. Aparte utiliza el número de filas modificado para enviar un mensaje al usuario:
``` scala
def deleteTaskCategoria(usuario: String, categoria: String) = Action {
   if(User.comprobarUsuario(usuario)){
      if(Categoria.comprobarCategoria(usuario,categoria)){
         Ok("Todas las tareas de la categoria "+categoria+" han sido borradas. Total: "+Task.deleteCategoria(usuario,categoria))
      } else {
         NotFound(errores("Error 404: La categoria "+categoria+" no existe para el usuario "+usuario)).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 7.2 Ejecución
* El formato de la URI para borrar es:
```
DELETE /{usuario}/categorias/{categoria}/tasks
```
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
DELETE /categorias/{categoria}/tasks
```
* Cuando se hayan borrado correctamente se mostrara el siguiente mensaje:
```
Todas las tareas de la categoria {categoria} han sido borradas. Total: {num rows}
```
* Si no hay ninguna categoria que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 8. Modificar la categoria de una tarea

#### 8.1 Implementación
* Para modificar la cateogria se utiliza el método `modificarCategoria` de la clase `Task`, que devuelva si se ha podido modificar o no la categoria:
``` scala
def modificarCategoria(usuario: String, nuevaCategoria: String, id: Long) : Boolean = (1 == DB.withConnection{
   implicit c => SQL("update task set categoria = {categoria} where usuario = {usuario} and id = {id}")
      .on("usuario" -> usuario, "categoria" -> nuevaCategoria, "id" -> id).executeUpdate()
})
```
* El método `updateCategoriaTask` llama al método anterior. Antes se comprueba que la categoria nueva no exista:
``` scala
def updateCategoriaTask(usuario: String) = Action { implicit request =>
   taskUpdateForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      update => {
         if(User.comprobarUsuario(usuario)){
            if(Categoria.comprobarCategoria(usuario,update._2)){
               if(Task.modificarCategoria(usuario,update._2,update._1)){
                  Ok("La tarea "+update._1+" del usuario "+usuario+" se ha trasladado a la categoria "+update._2+" correctamente")
               } else {
                  NotFound(errores("Error 404: La tarea con el id "+update._1+" no existe para el usuario "+usuario)).as("text/html")
               }
            } else {
               NotFound(errores("Error 404: La categoria "+update._2+" no existe para el usuario "+usuario)).as("text/html")
            }
         } else {
            NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
         }
      }
   )
}
``` 

#### 8.2 Ejecución
* El formato de la URI para borrar es:
```
POST /{usuario}/categorias/update/tasks 
```
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
POST /categorias/update/tasks 
```
* Cuando se hayan borrado correctamente se mostrara el siguiente mensaje:
```
La tarea {id} del usuario {usuario} se ha trasladado a la categoria {categoria} correctamente
```
* Si no hay ninguna categoria que concuerde o el usuario o el id no existen se devolverá el `ERROR 404`:
```
Error 404: La tarea con el id {id} no existe para el usuario {usuario}
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 9. Borrar la categoria de una tarea

#### 9.1 Implementación
* Para borrar la categoria se utiliza el método `modificarCategoria` de la clase `Task`, que devuelva si se ha podido modificar o no la categoria a **NULL**:
``` scala
def modificarCategoria(usuario: String, nuevaCategoria: String, id: Long) : Boolean = (1 == DB.withConnection{
   implicit c => SQL("update task set categoria = {categoria} where usuario = {usuario} and id = {id}")
      .on("usuario" -> usuario, "categoria" -> nuevaCategoria, "id" -> id).executeUpdate()
})
```
* El método `updateCategoriaTask` llama al método anterior. Antes se comprueba que la categoria nueva no exista:
``` scala
def deleteCategoriaTask(usuario: String, id: Long) = Action {
   if(User.comprobarUsuario(usuario)){
      if(None != Task.read(usuario,id)){
         Task.modificarCategoria(usuario,null,id)
         Ok("La tarea "+id+" se le ha borrado la categoria correctamente")
      } else {
         NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)).as("text/html")
      }
   } else {
      NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
   }
}
``` 

#### 9.2 Ejecución
* El formato de la URI para borrar es:
```
DELETE /{usuario}/tasks/{id}/deleteCategoria
```
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
DELETE /{usuario}/tasks/{id}/deleteCategoria
```
* Cuando se hayan borrado correctamente se mostrara el siguiente mensaje:
```
La tarea {id} se le ha borrado la categoria correctamente
```
* Si el usuario o el id no existen se devolverá el `ERROR 404`:
```
Error 404: La tarea con el id {id} no existe para el usuario {usuario}
Error 404: El usuario {usuario} no existe
```

## II. Test añadidos

### 1. Test Feature 1

Aqui se describen los tests para la primera feature.

#### 1.1 Test en el modelo

Todos los tests para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los tests:
```
val label = "Tarea test"
val nombreUsuario = "Test"
val nombreNuevoUsuario = "Nuevo test"
```

##### 1.1.1 Crear una tarea

* Se comprueba si se puede crear una tarea correctamente. Aparte tambien se comprueba si introduces el usuario incorrectamente.
* El `label` puede ser **null** ya que tal y como esta definido en la base de datos puede serlo. Por esta razon no se comprueba.
* Es necesario generar un usuario nuevo aunque en esta feature no esten definidos.
* El código del test es siguiente:
```
"crear tarea" in {  
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.crearUser(nombreUsuario)
      val idTest = Task.create(label,nombreUsuario,null)
      idTest must be_>(0L)

      Task.create(label,null,null) must throwA[JdbcSQLException]
      Task.create(label,"",null) must throwA[JdbcSQLException]
   }
}
```

##### 1.1.2 Todas las tareas

* Se comprueba si se listan correctamente todas las tareas de un usuario. Tambien se comprueba si el usuario no existe.
* El código del test es siguiente:
```
"todas las tareas" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.crearUser(nombreUsuario)

      Task.all(nombreUsuario) must be empty

      val idTest = Task.create(label,nombreUsuario,null)

      val lista = Task.all(nombreUsuario)
      lista must be have size(1)
      lista(0).id must_== idTest
      lista(0).label must_== label

      Task.all("") must be empty
   }
}
```

##### 1.1.3 Una tarea concreta

* Se comprueba si se encuentra una tarea para un usuario. Tambien se comprueba si el usuario no existe o el id de la tarea a buscar tampoco.
* El código del test es siguiente:
```
"una tarea concreta" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos una tarea de prueba
      User.crearUser(nombreUsuario)
      val idTest = Task.create(label,nombreUsuario,null)

      // Comprobamos la tarea
      val tarea = Task.read(nombreUsuario,idTest)
      tarea must beSome
      tarea.get.id must_== idTest
      tarea.get.label must_== label

      // Probamos a listar una tarea sin usuario ""
      Task.read("",idTest) must beNone
      // Probamos a listar una tarea sin usuario null
      Task.read(null,idTest) must beNone
      // Probamos a listar una tarea con otro id 0L
      Task.read(nombreUsuario,0L) must beNone
   }
}
```

##### 1.1.4 Borrado de una tarea

* Se comprueba si se borra una tarea para un usuario. Ademas comprobamos si no existe una tarea o un usuario.
* El código del test es siguiente:
```
"borrado de una tarea" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos una tarea de prueba
      User.crearUser(nombreUsuario)
      var idTest = Task.create(label,nombreUsuario,null)

      // Borramos la tarea creada
      val ok = Task.delete(nombreUsuario,idTest)
      ok must_== 1
      Task.all(nombreUsuario) must be empty

      // Intentamos volver a borrar la misma tarea
      Task.delete(nombreUsuario,idTest) must_== 0
      
      // Intentamos volver a borrar una tarea de un usuario que no existe
      idTest = Task.create(label,nombreUsuario,null)
      Task.delete(null,idTest) must_== 0
      idTest = Task.create(label,nombreUsuario,null)
      Task.delete("",idTest) must_== 0

      // Intentamos volver a borrar una tarea con otro id
      Task.delete(nombreUsuario,0L) must_== 0
   }
}
```

#### 1.2 Test en el controlador

Todos los tests para comprobar que el controlador funcione correctamente.

##### 1.2.1 Ruta incorrecta y ruta por defecto

* Comprobamos cuando una ruta se introduce incorrectamente:
```
 "devolver 404 en una ruta incorrecta" in {  
   running(FakeApplication()) {  
      route(FakeRequest(GET, "/incorrecto")) must beNone  
   }
}
```
* Y cuando se accede a la pagina por defecto:
```
"pagina por defecto" in {  
   running(FakeApplication()) {
      val Some(home) = route(FakeRequest(GET, "/"))

      status(home) must equalTo(SEE_OTHER)
      redirectLocation(home) must beSome("/tasks")
   }
}
```

##### 1.2.2 Todas las tareas

* Comprobamos que devuelva una lista en **json**:
```
"todas las tareas" in {
   running(FakeApplication()) {
      val Some(pag) = route(FakeRequest(GET,"/tasks"))

      status(pag) must equalTo(OK)  
      contentType(pag) must beSome.which(_ == "application/json")  
      contentAsString(pag) must contain ("anonimo") 
   }
}
```

##### 1.2.3 Una tarea concreta

* Comprobamos una tarea concreta. Tambien comprobamos que la tarea no exista:
```
"una tarea concreta" in {
   running(FakeApplication()) {
      val Some(pag) = route(FakeRequest(GET,"/tasks/1"))

      status(pag) must equalTo(OK)
      contentType(pag) must beSome.which(_ == "application/json")
      contentAsString(pag) must contain ("\"id\":1")

      // Comprobamos que una tarea no exista
      val Some(error) = route(FakeRequest(GET,"/tasks/0"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain ("404")
   }
}
```

##### 1.2.4 Crear una tarea

* Comprobamos la creacion de una tarea. Comprobamos si no se introducen los parametros en el formulario HTML correctamente:
```
"crear una tarea" in {
   running(FakeApplication()) {
      val Some(form) = route(FakeRequest(POST,"/tasks").withFormUrlEncodedBody(("label","Test")))

      status(form) must equalTo(CREATED)
      contentType(form) must beSome.which(_ == "application/json")
      contentAsString(form) must contain ("Test")

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/tasks").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")
   }
}
```

##### 1.2.5 Borrado de una tarea

* Comprobamos que se borre correctamente. Tambien se comprueba si la tarea no existe:
```
"borrado de una tarea" in {
   running(FakeApplication()) {
      val Some(del) = route(FakeRequest(DELETE,"/tasks/1"))

      status(del) must equalTo(OK)
      contentType(del) must beSome.which(_ == "text/plain")
      contentAsString(del) must contain("1")

      // Volvemos a borrar la tarea y deberia dar error
      val Some(error) = route(FakeRequest(DELETE,"/tasks/1"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
   }
}
```

### 2. Test Feature 2

#### 2.1 Test en el modelo

##### 2.1.1 Crear un usuario

* Se comprueba si se crean nuevos usuarios. Tambien se comprueba si ya existe.
* El código del test es siguiente:
```
"crear usuarios" in {  
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear un usuario
      // Comprobamos que se haya creado correctamente

      val idTest = User.crearUser(nombreUsuario)
      idTest must beSome

      // Probamos a volver a crearlo para comprobar que no se puede crear
      User.crearUser(nombreUsuario) must throwA[JdbcSQLException]
   }
}
```

##### 2.1.2 Leer un usuario

* Se comprueba la lectura de un usuario. Tambien se comprueba si el usuario no existe.
* El código del test es siguiente:
```
"extraer usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos el usuario
      val idTest = User.crearUser(nombreUsuario)
      idTest must beSome

      // LLamamos al modelo para leer un usuario
      // Comprobamos que se haya extraido correctamente

      val userTest = User.read(nombreUsuario)
      userTest must beSome
      userTest.get.id must equalTo(idTest.get)
      userTest.get.nombre must equalTo(nombreUsuario)

      //Probamos con un usuario que no deberia exista
      val userTest2 = User.read(nombreNuevoUsuario)
      userTest2 must beNone
   }
}
```

##### 2.1.3 Todos los usuarios

* Se comprueba que se devuelva una lista de todos los usuarios.
* El código del test es siguiente:
```
"todos los usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos el usuario
      val idTest = User.crearUser(nombreUsuario)
      idTest must beSome

      // LLamamos al modelo para leer todos los usuarios
      val users = User.all
      users.size must be_>=(1)
      users must contain(User(idTest.get,nombreUsuario))
   }
}
```

##### 2.1.4 Modificar un usuario

* Se comprueba si se modifica el nombre del usuario. Tambien se comprueba si no existe.
* El código del test es siguiente:
```
"modificar usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos el usuario
      val idTest = User.crearUser(nombreUsuario)
      idTest must beSome

      // Llamamos al modelo para modificarlo
      // Comprobamos el nuevo valor

      val result = User.modificarUser(nombreUsuario,nombreNuevoUsuario)
      result must beTrue

      // Repetimos para comprobar que no lo encuentra
      // y por lo tanto no puede modificarlo
      val result2 = User.modificarUser(nombreUsuario,nombreNuevoUsuario)
      result2 must beFalse
   }
}
```

##### 2.1.5 Borrar un usuario

* Se comprueba si se puede borrar un usuario. Tambien se comprueba si no existe.
* El código del test es siguiente:
```
"borrar usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      //Primero creamos un usuario
      val idTest = User.crearUser(nombreUsuario)
      idTest must beSome

      //Probamos a borrarlo

      val result = User.borrarUser(nombreUsuario)
      result must beTrue

      //Repetimos y da error
      val result2 = User.borrarUser(nombreUsuario)
      result2 must beFalse
   }
}
``` 
#### 2.2 Test en el controlador

##### 2.2.1 Todos las tareas de un usuario

* Comprobamos que devuelva una lista en **json**: 
```
"todas las tareas de un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks"))

      status(pag) must equalTo(OK)  
      contentType(pag) must beSome.which(_ == "application/json")  
      contentAsString(pag) must contain (usuarioTest)

      //Comprobamos un usuario que no exista
      val Some(error) = route(FakeRequest(GET,"/"+usuarioIncorrecto+"/tasks"))

      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
   }
}
```

##### 2.2.2 Crear una tarea para un usuario

* Comprobamos la creacion de una tarea. Comprobamos si no se introducen los parametros en el formulario HTML correctamente:
```
"crear una tarea para un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody(("label","Test")))

      status(form) must equalTo(CREATED)
      contentType(form) must beSome.which(_ == "application/json")
      contentAsString(form) must contain (usuarioTest)
      contentAsString(form) must contain ("\"label\":\"Test\"")

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")
   }
}
```

##### 2.2.3 Una tarea de un usuario

* Comprobamos una tarea concreta. Tambien comprobamos que la tarea no exista:
```
"una tarea concreta para un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody(("label","Test")))

      val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks/4"))

      status(pag) must equalTo(OK)
      contentType(pag) must beSome.which(_ == "application/json")
      contentAsString(pag) must contain ("\"label\":\"Test\"")

      // Comprobamos que una tarea no exista
      val Some(error) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks/0"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain ("404")
   }
}
```

##### 2.2.4 Borrado de una tarea de un usuario

* Comprobamos que se borre correctamente. Tambien se comprueba si la tarea no existe:
```
"borrado de una tarea de un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody(("label","Test")))

      val Some(del) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/4"))

      status(del) must equalTo(OK)
      contentType(del) must beSome.which(_ == "text/plain")
      contentAsString(del) must contain("4")

      // Volvemos a borrar la tarea y deberia dar error
      val Some(error) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/1"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
   }
}
```

### 3. Test Feature 3

#### 3.1 Test en el modelo

##### 3.1.1 Crear una tarea con fecha

* Comprobamos el crear una tarea con una fecha.
* El código del test es siguiente:
```
"crear una tarea con fecha" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear una tarea
      // Comprobamos que se haya creado correctamente

      User.crearUser(nombreUsuario)
      val idTest = Task.create(label,nombreUsuario,fecha)
      idTest must be_>(0L)
      val task = Task.read(nombreUsuario,idTest)
      dateFormat.format(task.get.fechaFin.get) must beEqualTo(dateFormat.format(fecha.get))
   }
}
```

##### 3.1.2 Borrar tareas por fecha

* Comprobamos el borrar una tarea hasta una fecha.
* El código del test es siguiente:
```
"borrar tareas por fecha" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear una tarea
      // Comprobamos que se haya creado correctamente

      User.crearUser(nombreUsuario)
      val idTest = Task.create(label,nombreUsuario,fecha)

      val result = Task.deleteDate(nombreUsuario,fecha.get)
      result must_== 1
      Task.all(nombreUsuario) must be empty

      // Intentamos volver a borrar una tarea con la misma fecha
      Task.deleteDate(nombreUsuario,fecha.get) must_== 0
   }
}
```

##### 3.1.3 Mostrar tareas por fecha

* Se comprueba que se devuelva una lista de todas las tareas hasta una fecha.
* El código del test es siguiente:
```
"mostrar tareas por fecha" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear una tarea
      // Comprobamos que se haya creado correctamente

      User.crearUser(nombreUsuario)

      Task.all(nombreUsuario,fecha.get) must be empty

      val idTest = Task.create(label,nombreUsuario,fecha)

      // Comprobamos todas las tareas
      val lista = Task.all(nombreUsuario,fecha.get)
      lista must be have size(1)
      lista(0).id must_== idTest
      lista(0).label must_== label
   }
}
```

#### 3.2 Test en el controlador

##### 3.2.1 Crear una tarea con fecha

* Creamos una tarea con una fecha. Comprobamos si se introduce una fecha en un formato incorrecto:
```
"crear una tarea con fecha" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("label","Test"),("fechaFin",fecha)))

      status(form) must equalTo(CREATED)
      contentType(form) must beSome.which(_ == "application/json")
      contentAsString(form) must contain (usuarioTest)
      contentAsString(form) must contain ("\"label\":\"Test\"")
      val dateFormat = new SimpleDateFormat("dd-MM-yyy");
      val parsedDate = dateFormat.parse(fecha);
      val timestamp = new Timestamp(parsedDate.getTime());
      (contentAsJson(form) \ usuarioTest \\ "fechaFin").map(_.as[Date]) must contain (timestamp)

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("fechaFin",fechaIncorrecta)))
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")
   }
}
```

##### 3.2.2 Borrado de tareas por fecha

* Se comprueba que se borren las tareas. Tambien se comprueba si el usuario no existe o la fecha no tiene el formato correcto:
```
"borrado de tareas por fecha" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("label","Test"),("fechaFin",fecha)))
      val Some(form2) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("label","Test 2"),("fechaFin",fecha)))

      val Some(del) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/"+fecha))

      status(del) must equalTo(OK)
      contentType(del) must beSome.which(_ == "text/plain")
      contentAsString(del) must contain ("2")
      contentAsString(del) must contain (usuarioTest)
      contentAsString(del) must contain (fecha)

      //Usuario incorrecto
      val Some(error) = route(FakeRequest(DELETE,"/"+usuarioIncorrecto+"/tasks/"+fecha))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
      contentAsString(error) must contain(usuarioIncorrecto)

      //Fecha incorrecta
      val Some(error2) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/"+fechaIncorrecta))
      status(error2) must equalTo(BAD_REQUEST)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("400")
      contentAsString(error2) must contain(fechaIncorrecta)
   }
}
```

##### 3.2.3 Mostrar tareas por fecha

* Comprobamos que se devuelve el **json** correctamente. Tambien se comprueba si el usuario no existe o la fecha no tiene el formato correcto:
```
"mostrar tareas por fecha" in {
   running(FakeApplication()) {
      User.crearUser(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("label","Test"),("fechaFin",fecha)))
      val Some(form2) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").
         withFormUrlEncodedBody(("label","Test 2"),("fechaFin",fecha)))

      val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks/finalizadas/"+fecha))

      status(pag) must equalTo(OK)
      contentType(pag) must beSome.which(_ == "application/json")
      contentAsString(pag) must contain("Test")
      contentAsString(pag) must contain(usuarioTest)

      //Usuario incorrecto
      val Some(error) = route(FakeRequest(GET,"/"+usuarioIncorrecto+"/tasks/finalizadas/"+fecha))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
      contentAsString(error) must contain(usuarioIncorrecto)

      //Fecha incorrecta
      val Some(error2) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks/finalizadas/"+fechaIncorrecta))
      status(error2) must equalTo(BAD_REQUEST)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("400")
      contentAsString(error2) must contain(fechaIncorrecta)
   }
}
```


## III. Repositorios

### Bitbucket
- Enlace a [Bitbucket](https://bitbucket.org/JoseVte/play-todolist)

### Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)