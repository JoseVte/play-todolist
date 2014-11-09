# Informe técnico de la practica 2

En este informe se van a describir todas las funcionalidades nuevas y los test para las funcionalidades antiguas.
[Aqui](/doc/practica1.md) se encuentra la documentación referentes a las funcionalidades antiguas.

<!-- MarkdownTOC -->

- [I. Funcionalidades principales](#i-funcionalidades-principales)
   - [1. Crear categoría](#1-crear-categoría)
      - [1.1 Implementación](#11-implementación)
      - [1.2 Ejecución](#12-ejecución)
   - [2. Mostrar todas las categorías](#2-mostrar-todas-las-categorías)
      - [2.1 Implementación](#21-implementación)
      - [2.2 Ejecución](#22-ejecución)
   - [3. Modificar el nombre de la categoría](#3-modificar-el-nombre-de-la-categoría)
      - [3.1 Implementación](#31-implementación)
      - [3.2 Ejecución](#32-ejecución)
   - [4. Borrar la categoría](#4-borrar-la-categoría)
      - [4.1 Implementación](#41-implementación)
      - [4.2 Ejecución](#42-ejecución)
   - [5. Añadir tareas a categoría](#5-añadir-tareas-a-categoría)
      - [5.1 Implementación](#51-implementación)
      - [5.2 Ejecución](#52-ejecución)
   - [6. Listado de tareas](#6-listado-de-tareas)
      - [6.1 Implementación](#61-implementación)
      - [6.2 Ejecución](#62-ejecución)
   - [7. Quitar todas las tareas de una categoría](#7-quitar-todas-las-tareas-de-una-categoría)
      - [7.1 Implementación](#71-implementación)
      - [7.2 Ejecución](#72-ejecución)
   - [8. Modificar la categoría de una tarea](#8-modificar-la-categoría-de-una-tarea)
      - [8.1 Implementación](#81-implementación)
      - [8.2 Ejecución](#82-ejecución)
   - [9. Borrar la categoría de una tarea](#9-borrar-la-categoría-de-una-tarea)
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
   - [4. Test Feature 4](#4-test-feature-4)
      - [4.1 Test en el modelo](#41-test-en-el-modelo)
         - [4.1.1 Crear una categoría](#411-crear-una-categoría)
         - [4.1.2 Mostrar las categorías de un usuario](#412-mostrar-las-categorías-de-un-usuario)
         - [4.1.3 Modificar el nombre de una categoría](#413-modificar-el-nombre-de-una-categoría)
         - [4.1.4 Borrar una categoría](#414-borrar-una-categoría)
         - [4.1.5 Añadir una tarea con categoría](#415-añadir-una-tarea-con-categoría)
         - [4.1.6 Mostrar las tareas de una categoría](#416-mostrar-las-tareas-de-una-categoría)
         - [4.1.7 Borrar tareas de una categoría](#417-borrar-tareas-de-una-categoría)
         - [4.1.8 Modificar la categoría de una tarea](#418-modificar-la-categoría-de-una-tarea)
      - [4.2 Test en el controlador](#42-test-en-el-controlador)
         - [4.2.1 Todas las tareas de un usuario](#421-todas-las-tareas-de-un-usuario)
         - [4.2.2 Crear una categoría](#422-crear-una-categoría)
         - [4.2.3 Actualizar el nombre de una categoría](#423-actualizar-el-nombre-de-una-categoría)
         - [4.2.4 Borrar una categoría](#424-borrar-una-categoría)
         - [4.2.5 Crear una tarea con una categoría](#425-crear-una-tarea-con-una-categoría)
         - [4.2.6 Mostrar todas las tareas de una categoría](#426-mostrar-todas-las-tareas-de-una-categoría)
         - [4.2.7 Borrar todas las tareas de una categoría](#427-borrar-todas-las-tareas-de-una-categoría)
         - [4.2.8 Modificar la categoría de una tarea](#428-modificar-la-categoría-de-una-tarea)
         - [4.2.9 Borrar la categoría de una tarea](#429-borrar-la-categoría-de-una-tarea)
- [III. Repositorios](#iii-repositorios)
   - [GitHub](#github)
   - [Bitbucket](#bitbucket)
   - [Heroku](#heroku)

<!-- /MarkdownTOC -->

## I. Funcionalidades principales

### 1. Crear categoría

#### 1.1 Implementación

* El método `create` de la clase `Categoria` permite crear una categoría nueva a partir de un `usuario` y un `nombre`:
``` scala
def create(usuario: String, nombreCategoria: String): Int = DB.withConnection {
   implicit c => SQL("INSERT INTO categorias (usuario,nombreCategoria) VALUES ({usuario},{nombreCategoria})")
      .on("usuario" -> usuario,"nombreCategoria" -> nombreCategoria).executeUpdate()
}
```
* En el controlador existe el método `newCategoria` que recibe el `usuario` y devuelve el **JSON** de la categoría creada. Se comprueba si el usuario existe o si la categoría ya existe:
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
* Si la categoría ya existe devuelve un `ERROR 400`:
```
Error 400: La categoria {categoria} ya existe
```

### 2. Mostrar todas las categorías

#### 2.1 Implementación

* El método `all` de la clase `Categoria` permite acceder a todas las categorías del `usuario` y devolver un `Option[Task]`:
``` scala
def all(usuario: String): List[Categoria] = DB.withConnection {
   implicit c => SQL("SELECT * FROM categorias WHERE usuario = {usuario}")
      .on("usuario" -> usuario).as(categoria *)
}
```
* En el controlador existe el método `categorías` que recibe el `usuario` y devuelve el **JSON** de con todas las categorías de ese usuario:
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

### 3. Modificar el nombre de la categoría

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
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
POST /categorias/update
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna categoría que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no se ha podido modificar porque no se encuentra
Error 404: El usuario {usuario} no existe
```
* Tambien puede fallar si ya existe la categoría:
```
"Error 400: La categoria {categoria} no se ha podido modificar porque el nuevo nombre ya existe"
```

### 4. Borrar la categoría

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
* También se permite borrar las tareas para el usuario anónimo, pero está en desuso:
```
DELETE /categorias/{categoria}
```
* Cuando se haya borrado correctamente se mostrara el siguiente mensaje:
```
Tarea {id} del usuario {usuario} borrada correctamente
```
* Si no hay ninguna categoría que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 5. Añadir tareas a categoría

#### 5.1 Implementación

* Para crear una `tarea` que este dentro de una `categoria` se utiliza el método `create` de la clase `Task`:
```
def create(label: String, usuario: String, fechaFin: Option[Date] = None, categoria: Option[String] = None) : Long = DB.withConnection{
   implicit c => SQL("insert into task(label,usuario,fechaFin, categoria) values ({label},{usuario},{fechaFin},{categoria})")
      .on("label" -> label, "usuario" -> usuario, "categoria" -> categoria, "fechaFin" -> fechaFin).executeInsert().get
}
```
* El método `newTask` recibe un `usuario` por parámetro y una `descripción` de una tarea, una `categoria` y la `fecha de finalización` por un formulario `POST`. Devuelve un código `201` con un **JSON** con los datos del nuevo objeto creado:
```
def newTask(usuario: String) = Action { implicit request =>
   taskForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      task => {
         if(User.comprobarUsuario(usuario)){
            val id = Task.create(task.label,usuario,task.fechaFin,task.categoria)
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
* Para el acceso a la base de datos se utiliza un método de la clase `Task` que permite crear una lista de tareas a partir de un usuario y una categoría:
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
* Si no hay ninguna categoría que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 7. Quitar todas las tareas de una categoría

#### 7.1 Implementación
* Para borrar las tareas se utiliza el método `deleteCategoria` de la clase `Task`, que devuelva el número de tareas que se han borrado hasta en la categoría:
``` scala
def deleteCategoria(usuario: String, categoria: String) : Int = DB.withConnection{
   implicit c => SQL("delete from task where usuario = {usuario} and categoria = {categoria}")
      .on("usuario" -> usuario, "categoria" -> categoria).executeUpdate()
}
```
* El método `deleteTaskCategoria` llama al método anterior para realizar el borrado. Antes se comprueba que la categoría exista. Aparte utiliza el número de filas modificado para enviar un mensaje al usuario:
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
* Si no hay ninguna categoría que concuerde o el usuario no existe se devolverá el `ERROR 404`:
```
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 8. Modificar la categoría de una tarea

#### 8.1 Implementación
* Para modificar la catenaria se utiliza el método `updateCategoria` de la clase `Task`, que devuelva si se ha podido modificar o no la categoria:
``` scala
def updateCategoria(usuario: String, nuevaCategoria: String, id: Long) : Boolean = (1 == DB.withConnection{
   implicit c => SQL("update task set categoria = {categoria} where usuario = {usuario} and id = {id}")
      .on("usuario" -> usuario, "categoria" -> nuevaCategoria, "id" -> id).executeUpdate()
})
```
* El método `updateCategoriaTask` llama al método anterior. Antes se comprueba que la categoría nueva no exista:
``` scala
def updateCategoriaTask(usuario: String) = Action { implicit request =>
   taskUpdateForm.bindFromRequest.fold(
      errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
      update => {
         if(User.comprobarUsuario(usuario)){
            if(Categoria.comprobarCategoria(usuario,update._2)){
               if(Task.updateCategoria(usuario,update._2,update._1)){
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
* Si no hay ninguna categoría que concuerde o el usuario o el id no existen se devolverá el `ERROR 404`:
```
Error 404: La tarea con el id {id} no existe para el usuario {usuario}
Error 404: La categoria {categoria} no existe
Error 404: El usuario {usuario} no existe
```

### 9. Borrar la categoría de una tarea

#### 9.1 Implementación
* Para borrar la categoria se utiliza el método `updateCategoria` de la clase `Task`, que devuelva si se ha podido modificar o no la categoría a **NULL**:
``` scala
def updateCategoria(usuario: String, nuevaCategoria: String, id: Long) : Boolean = (1 == DB.withConnection{
   implicit c => SQL("update task set categoria = {categoria} where usuario = {usuario} and id = {id}")
      .on("usuario" -> usuario, "categoria" -> nuevaCategoria, "id" -> id).executeUpdate()
})
```
* El método `updateCategoriaTask` llama al método anterior. Antes se comprueba que la categoría nueva no exista:
``` scala
def deleteCategoriaTask(usuario: String, id: Long) = Action {
   if(User.comprobarUsuario(usuario)){
      if(None != Task.read(usuario,id)){
         Task.updateCategoria(usuario,null,id)
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
La tarea {id} se le ha borrado la categoría correctamente
```
* Si el usuario o el id no existen se devolverá el `ERROR 404`:
```
Error 404: La tarea con el id {id} no existe para el usuario {usuario}
Error 404: El usuario {usuario} no existe
```

## II. Test añadidos

### 1. Test Feature 1

Aquí se describen los test para la primera feature.

#### 1.1 Test en el modelo

Todos los test para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los test:
```
val label = "Tarea test"
val nombreUsuario = "Test"
```

##### 1.1.1 Crear una tarea

* Se comprueba si se puede crear una tarea correctamente. Aparte también se comprueba si introduces el usuario incorrectamente.
* El `label` puede ser **null** ya que tal y como esta definido en la base de datos puede serlo. Por esta razón no se comprueba.
* Es necesario generar un usuario nuevo aunque en esta feature no estén definidos.
* El código del test es siguiente:
```
"crear tarea" in {  
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val idTest = Task.create(label,nombreUsuario,null)
      idTest must be_>(0L)

      Task.create(label,null,null) must throwA[JdbcSQLException]
      Task.create(label,"",null) must throwA[JdbcSQLException]
   }
}
```

##### 1.1.2 Todas las tareas

* Se comprueba si se listan correctamente todas las tareas de un usuario. También se comprueba si el usuario no existe.
* El código del test es siguiente:
```
"todas las tareas" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)

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

* Se comprueba si se encuentra una tarea para un usuario. También se comprueba si el usuario no existe o el id de la tarea a buscar tampoco.
* El código del test es siguiente:
```
"una tarea concreta" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos una tarea de prueba
      User.create(nombreUsuario)
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
      User.create(nombreUsuario)
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

* Comprobamos una tarea concreta. También comprobamos que la tarea no exista:
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

* Comprobamos la creación de una tarea. Comprobamos si no se introducen los parámetros en el formulario HTML correctamente:
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

* Comprobamos que se borre correctamente. También se comprueba si la tarea no existe:
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

Aquí se describen los test para la segunda feature.

#### 2.1 Test en el modelo

Todos los test para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los test:
```
val nombreNuevoUsuario = "Nuevo test"
```

##### 2.1.1 Crear un usuario

* Se comprueba si se crean nuevos usuarios. También se comprueba si ya existe.
* El código del test es siguiente:
```
"crear usuarios" in {  
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear un usuario
      // Comprobamos que se haya creado correctamente

      val idTest = User.create(nombreUsuario)
      idTest must beSome

      // Probamos a volver a crearlo para comprobar que no se puede crear
      User.create(nombreUsuario) must throwA[JdbcSQLException]
   }
}
```

##### 2.1.2 Leer un usuario

* Se comprueba la lectura de un usuario. También se comprueba si el usuario no existe.
* El código del test es siguiente:
```
"extraer usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos el usuario
      val idTest = User.create(nombreUsuario)
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
      val idTest = User.create(nombreUsuario)
      idTest must beSome

      // LLamamos al modelo para leer todos los usuarios
      val users = User.all
      users.size must be_>=(1)
      users must contain(User(idTest.get,nombreUsuario))
   }
}
```

##### 2.1.4 Modificar un usuario

* Se comprueba si se modifica el nombre del usuario. También se comprueba si no existe.
* El código del test es siguiente:
```
"modificar usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // Primero creamos el usuario
      val idTest = User.create(nombreUsuario)
      idTest must beSome

      // Llamamos al modelo para modificarlo
      // Comprobamos el nuevo valor

      val result = User.update(nombreUsuario,nombreNuevoUsuario)
      result must beTrue

      // Repetimos para comprobar que no lo encuentra
      // y por lo tanto no puede modificarlo
      val result2 = User.update(nombreUsuario,nombreNuevoUsuario)
      result2 must beFalse
   }
}
```

##### 2.1.5 Borrar un usuario

* Se comprueba si se puede borrar un usuario. También se comprueba si no existe.
* El código del test es siguiente:
```
"borrar usuarios" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      //Primero creamos un usuario
      val idTest = User.create(nombreUsuario)
      idTest must beSome

      //Probamos a borrarlo

      val result = User.delete(nombreUsuario)
      result must beTrue

      //Repetimos y da error
      val result2 = User.delete(nombreUsuario)
      result2 must beFalse
   }
}
```

#### 2.2 Test en el controlador

Todos los test para comprobar que el controlador funcione correctamente.
Para los test se utilizan las siguientes variables:
```
val usuarioTest="Test"
val usuarioIncorrecto="Error"
```

##### 2.2.1 Todos las tareas de un usuario

* Comprobamos que devuelva una lista en **json**: 
```
"todas las tareas de un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

* Comprobamos la creación de una tarea. Comprobamos si no se introducen los parámetros en el formulario HTML correctamente:
```
"crear una tarea para un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

* Comprobamos una tarea concreta. También comprobamos que la tarea no exista:
```
"una tarea concreta para un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

* Comprobamos que se borre correctamente. También se comprueba si la tarea no existe:
```
"borrado de una tarea de un usuario" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

Aquí se describen los test para la tercera feature.

#### 3.1 Test en el modelo

Todos los test para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los test:
```
val fecha:Option[Date] = Some(new Date)
val dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
```

##### 3.1.1 Crear una tarea con fecha

* Comprobamos el crear una tarea con una fecha.
* El código del test es siguiente:
```
"crear una tarea con fecha" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // LLamamos al modelo para crear una tarea
      // Comprobamos que se haya creado correctamente

      User.create(nombreUsuario)
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

      User.create(nombreUsuario)
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

      User.create(nombreUsuario)

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

Todos los test para comprobar que el controlador funcione correctamente.
Para los test se utilizan las siguientes variables:
```
val fecha="24-10-2014"
val fechaIncorrecta="24-10"
```

##### 3.2.1 Crear una tarea con fecha

* Creamos una tarea con una fecha. Comprobamos si se introduce una fecha en un formato incorrecto:
```
"crear una tarea con fecha" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

* Se comprueba que se borren las tareas. También se comprueba si el usuario no existe o la fecha no tiene el formato correcto:
```
"borrado de tareas por fecha" in {
   running(FakeApplication()) {
      // Se comprueba en otros test esta funcionalidad
      User.create(usuarioTest)
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

* Comprobamos que se devuelve el **json** correctamente. También se comprueba si el usuario no existe o la fecha no tiene el formato correcto:
```
"mostrar tareas por fecha" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
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

### 4. Test Feature 4

Aquí se describen los test para la cuarta feature.

#### 4.1 Test en el modelo

Todos los test para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los test:
```
val nombreCategoria = "CategoriaTest"
val nombreNuevoCategoria = "NuevaCategoriaTest"
```

##### 4.1.1 Crear una categoría

* Comprobamos el crear una categoría. También se comprueba si ya existía o el usuario no existe.
* El código del test es siguiente:
```
"crear categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)

      cat must be_>(0)

      // Probamos a crear una cateogria sin usuario null
      Categoria.create(null,nombreCategoria) must throwA[JdbcSQLException]

      //Probamos a crear una categoria que ya existe
      Categoria.create(nombreUsuario,nombreCategoria) must throwA[JdbcSQLException]
   }
}
```

##### 4.1.2 Mostrar las categorías de un usuario

* Comprobamos si se muestran todas las categorías de un usuario.
* El código del test es siguiente:
```
"mostrar categorias" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)

      val cats = Categoria.all(nombreUsuario)
      cats must be have size(1)
      cats(0).usuario must_== nombreUsuario
      cats(0).nombreCategoria must_== nombreCategoria

      // Probamos a listar una tarea sin usuario ""
      Categoria.all("") must be empty
   }
}
```

##### 4.1.3 Modificar el nombre de una categoría

* Se comprueba que se modifica el nombre de una categoría. También se comprueba si ya existía la categoría a la que se va a modificar.
* El código del test es siguiente:
```
"modificar categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)

      val ok = Categoria.update(nombreUsuario,nombreCategoria,nombreNuevoCategoria)
      ok must beTrue

      val error = Categoria.update(nombreUsuario,nombreCategoria,nombreNuevoCategoria)
      error must beFalse
   }
}
```

##### 4.1.4 Borrar una categoría

* Se comprueba que se borra una categoría. También se comprueba si la categoría no existe.
* El código del test es siguiente:
```
"borrar categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)

      val ok = Categoria.delete(nombreUsuario,nombreCategoria)
      ok must beTrue

      val error = Categoria.delete(nombreUsuario,nombreCategoria)
      error must beFalse
   }
}
```

##### 4.1.5 Añadir una tarea con categoría

* Se comprueba que se crea una tarea dentro de una categoría. También se comprueba si la categoría no existe.
* El código del test es siguiente:
```
"añadir tareas a categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)

      val idTest = Task.create(label,nombreUsuario,None,Some(nombreCategoria))
      idTest must be_>(0L)

      // Probamos a crear una tarea sin categoria null
      Task.create(label,nombreUsuario,None) must be_>(0L)

      // Probamos a crear una tarea con una categoria que no existe
      Task.create(label,nombreUsuario,None,Some(nombreNuevoCategoria)) must throwA[JdbcSQLException]
   }
}
```

##### 4.1.6 Mostrar las tareas de una categoría

* Se comprueba que se muestran las tareas de la categoría. También se comprueba si la categoría no existe.
* El código del test es siguiente:
```
"mostrar todas las tareas de una categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)
      val idTest = Task.create(label,nombreUsuario,None,Some(nombreCategoria))

      val tareas = Task.all(nombreUsuario,nombreCategoria)
      tareas must be have size(1)
      tareas(0).id must_== idTest
      tareas(0).label must_== label

      // Probamos a listar una tarea sin usuario ""
      Task.all(nombreUsuario,"") must be empty
   }
}
```

##### 4.1.7 Borrar tareas de una categoría

* Se comprueba que se borra las tareas de una categoría.
* El código del test es siguiente:
```
"quitar tareas de una categoria" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      User.create(nombreUsuario)
      val cat = Categoria.create(nombreUsuario,nombreCategoria)
      val idTest = Task.create(label,nombreUsuario,None,Some(nombreCategoria))

      val ok = Task.deleteCategoria(nombreUsuario,nombreCategoria)
      ok  must equalTo(1)

      val error = Task.deleteCategoria(nombreUsuario,nombreCategoria)
      error must equalTo(0)
   }
}
```

##### 4.1.8 Modificar la categoría de una tarea

* Se comprueba que si se modifica la categoría de la tarea. También se comprueba si la categoría nueva no existe.
* El código del test es siguiente:
```
"modificar la categoria de una tarea" in {
   running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      User.create(nombreUsuario)
      Categoria.create(nombreUsuario,nombreCategoria)
      Categoria.create(nombreUsuario,nombreNuevoCategoria)
      val idTest = Task.create(label,nombreUsuario,None,Some(nombreCategoria))

      val ok = Task.updateCategoria(nombreUsuario,nombreNuevoCategoria,idTest)
      ok must beTrue

      Task.updateCategoria(nombreUsuario,"noExiste",idTest) must throwA[JdbcSQLException]
   }
}
```

#### 4.2 Test en el controlador

Todos los test para comprobar que el controlador funcione correctamente.
Para los test se utilizan las siguientes variables:
```
val categoriaTest="CategoriaTest"
val categoriaNuevaTest="CategoriaNuevaTest"
val categoriaIncorrectaTest="CategoriaIncorrectaTest"
```

##### 4.2.1 Todas las tareas de un usuario

* Comprobamos que devuelve una lista en **JSON**. También se comprueba si el usuario no existe:
```
"todas las categorias de un usuario" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/categorias"))

      status(pag) must equalTo(OK)
      contentType(pag) must beSome.which(_ == "application/json")
      contentAsString(pag) must contain (usuarioTest)

      // Usuario incorrecto
      val Some(error) = route(FakeRequest(GET,"/"+usuarioIncorrecto+"/categorias"))

      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
   }
}
```

##### 4.2.2 Crear una categoría

* Creamos una categoría. También comprobamos todos los posibles errores:
```
"crear una categoria de un usuario" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias")
         .withFormUrlEncodedBody(("categoria",categoriaTest)))

      status (form) must equalTo(CREATED)
      contentType(form) must beSome.which(_ == "application/json")
      contentAsString(form) must contain (usuarioTest)
      contentAsString(form) must contain (categoriaTest)

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")

      // El usuario no existe
      val Some(error2) = route(FakeRequest(POST,"/"+usuarioIncorrecto+"/categorias")
         .withFormUrlEncodedBody(("categoria",categoriaTest)))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")

      // Repetir nombre de la categoria
      val Some(error3) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias")
         .withFormUrlEncodedBody(("categoria",categoriaTest)))
      status(error3) must equalTo(BAD_REQUEST)
      contentType(error3) must beSome.which(_ == "text/html")
      contentAsString(error3) must contain("400")
   }
}
```

##### 4.2.3 Actualizar el nombre de una categoría

* Actualizamos el nombre una categoría. También comprobamos todos los posibles errores:
```
"actualizar nombre de una categoria" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(update) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update")
         .withFormUrlEncodedBody(("categoriaAnt",categoriaTest),("categoriaNueva",categoriaNuevaTest)))

      status(update) must equalTo(OK)
      contentType(update) must beSome.which(_ == "text/plain")
      contentAsString(update) must contain ("correctamente")

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")

      // El usuario no existe
      val Some(error2) = route(FakeRequest(POST,"/"+usuarioIncorrecto+"/categorias/update")
         .withFormUrlEncodedBody(("categoriaAnt",categoriaTest),("categoriaNueva",categoriaNuevaTest)))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")

      // No existe la categoria a modificar
      val Some(error3) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update")
         .withFormUrlEncodedBody(("categoriaAnt",categoriaTest),("categoriaNueva",categoriaTest)))
      status(error3) must equalTo(NOT_FOUND)
      contentType(error3) must beSome.which(_ == "text/html")
      contentAsString(error3) must contain("404")

      // Ya existe el nombre nuevo categoria a modificar
      Categoria.create(usuarioTest,categoriaTest)
      val Some(error4) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update")
         .withFormUrlEncodedBody(("categoriaAnt",categoriaNuevaTest),("categoriaNueva",categoriaTest)))
      status(error4) must equalTo(BAD_REQUEST)
      contentType(error4) must beSome.which(_ == "text/html")
      contentAsString(error4) must contain("400")
   }
}
```

##### 4.2.4 Borrar una categoría

* Borramos una categoría. También comprobamos todos los posibles errores:
```
"borrar una categoria" in{
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(delete) = route(FakeRequest(DELETE,"/"+usuarioTest+"/categorias/"+categoriaTest))

      status(delete) must equalTo(OK)
      contentType(delete) must beSome.which(_ == "text/plain")
      contentAsString(delete) must contain ("correctamente")

      // El usuario no existe
      val Some(error) = route(FakeRequest(DELETE,"/"+usuarioIncorrecto+"/categorias/"+categoriaTest))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")

      // No existe la categoria a modificar
      val Some(error2) = route(FakeRequest(DELETE,"/"+usuarioTest+"/categorias/"+categoriaNuevaTest))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")
   }
}
```

##### 4.2.5 Crear una tarea con una categoría

* Crear una tarea con una categoría. También comprobamos todos los posibles errores:
```
"añadir una tarea a una categoria" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)

      val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks")
         .withFormUrlEncodedBody(("label","Test"),("categoria",categoriaTest)))

      status(form) must equalTo(CREATED)
      contentType(form) must beSome.which(_ == "application/json")
      contentAsString(form) must contain ("\"label\":\"Test\"")

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/tasks").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")

      val Some(error2) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks")
         .withFormUrlEncodedBody(("label","Test"),("categoria",categoriaIncorrectaTest)))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")
   }
}
```

##### 4.2.6 Mostrar todas las tareas de una categoría

* Muestra las tareas de una categoría. También comprobamos todos los posibles errores:
```
"mostrar tareas de una categoria" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/categorias/"+categoriaTest+"/tasks"))

      status(pag) must equalTo(OK)
      contentType(pag) must beSome.which(_ == "application/json")
      contentAsString(pag) must contain("Test")

       //Comprobamos un usuario que no exista
      val Some(error) = route(FakeRequest(GET,"/"+usuarioIncorrecto+"/categorias/"+categoriaTest+"/tasks"))

      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")

       //Comprobamos una categoria que no exista
      val Some(error2) = route(FakeRequest(GET,"/"+usuarioTest+"/categorias/"+categoriaNuevaTest+"/tasks"))

      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")
   }
}
```

##### 4.2.7 Borrar todas las tareas de una categoría

* Borra las tareas de una categoría. También comprobamos todos los posibles errores:
```
"quitar todas las tareas de una categoria" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(delete) = route(FakeRequest(DELETE,"/"+usuarioTest+"/categorias/"+categoriaTest+"/tasks"))

      status(delete) must equalTo(OK)
      contentType(delete) must beSome.which(_ == "text/plain")
      contentAsString(delete) must contain (categoriaTest)
      contentAsString(delete) must contain ("Total: 1")

      // El usuario no existe
      val Some(error) = route(FakeRequest(DELETE,"/"+usuarioIncorrecto+"/categorias/"+categoriaTest+"/tasks"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")

      // No existe la categoria a modificar
      val Some(error2) = route(FakeRequest(DELETE,"/"+usuarioTest+"/categorias/"+categoriaNuevaTest+"/tasks"))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")
   }
}
```

##### 4.2.8 Modificar la categoría de una tarea

* Modifica la categoría de una tarea. También comprobamos todos los posibles errores:
```
"modificar la categoria de una tarea" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      Categoria.create(usuarioTest,categoriaNuevaTest)
      val idTest = Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(update) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update/tasks")
         .withFormUrlEncodedBody(("id",""+idTest),("categoriaNueva",categoriaNuevaTest)))

      status(update) must equalTo(OK)
      contentType(update) must beSome.which(_ == "text/plain")
      contentAsString(update) must contain ("correctamente")

      // El formulario esta mal introducido
      val Some(error) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update/tasks").withFormUrlEncodedBody())
      status(error) must equalTo(BAD_REQUEST)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("400")

      // El usuario no existe
      val Some(error2) = route(FakeRequest(POST,"/"+usuarioIncorrecto+"/categorias/update/tasks")
         .withFormUrlEncodedBody(("id",""+idTest),("categoriaNueva",categoriaNuevaTest)))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")

      // No existe el id de la tarea
      val Some(error3) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update/tasks")
         .withFormUrlEncodedBody(("id","9999"),("categoriaNueva",categoriaTest)))
      status(error3) must equalTo(NOT_FOUND)
      contentType(error3) must beSome.which(_ == "text/html")
      contentAsString(error3) must contain("404")

      // La categoria nueva no pertenece a ese usuario
      User.create(usuarioIncorrecto)
      Categoria.create(usuarioIncorrecto,categoriaIncorrectaTest)
      val Some(error5) = route(FakeRequest(POST,"/"+usuarioTest+"/categorias/update/tasks")
         .withFormUrlEncodedBody(("id",""+idTest),("categoriaNueva",categoriaIncorrectaTest)))
      status(error5) must equalTo(NOT_FOUND)
      contentType(error5) must beSome.which(_ == "text/html")
      contentAsString(error5) must contain("404")
   }
}
```

##### 4.2.9 Borrar la categoría de una tarea

* Borra la categoría de una tarea. También comprobamos todos los posibles errores:
```
"borrar la categoria de una tarea" in {
   running(FakeApplication()) {
      User.create(usuarioTest)
      Categoria.create(usuarioTest,categoriaTest)
      val idTest = Task.create("Test",usuarioTest,None,Some(categoriaTest))

      val Some(delete) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/"+idTest+"/deleteCategoria"))

      status(delete) must equalTo(OK)
      contentType(delete) must beSome.which(_ == "text/plain")
      contentAsString(delete) must contain (""+idTest)

      // El usuario no existe
      val Some(error) = route(FakeRequest(DELETE,"/"+usuarioIncorrecto+"/tasks/"+idTest+"/deleteCategoria"))
      status(error) must equalTo(NOT_FOUND)
      contentType(error) must beSome.which(_ == "text/html")
      contentAsString(error) must contain("404")

      // No existe la tarea a modificar
      val Some(error2) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/9999/deleteCategoria"))
      status(error2) must equalTo(NOT_FOUND)
      contentType(error2) must beSome.which(_ == "text/html")
      contentAsString(error2) must contain("404")
   }
}
```

## III. Repositorios

### GitHub
- Enlace a [GitHub](https://github.com/JoseVte/play-todolist)

### Bitbucket
- Enlace a [Bitbucket](https://bitbucket.org/JoseVte/play-todolist)

### Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)