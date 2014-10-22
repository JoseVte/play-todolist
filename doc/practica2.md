# Informe técnico de la practica 2

En este informe se van a describir todas las funcionalidades nuevas y los tests para las funcionalidades antiguas.
[Aqui](/doc/practica1.md) se encuentra la documentación referentes a las funcionalidades antiguas.

<!-- MarkdownTOC -->

- [I. Funcionalidades principales](#i-funcionalidades-principales)
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
- [III. Repositorios](#iii-repositorios)
    - [Bitbucket](#bitbucket)
    - [Heroku](#heroku)

<!-- /MarkdownTOC -->

## I. Funcionalidades principales

## II. Test añadidos

### 1. Test Feature 1

Aqui se describen los tests para la primera feature.

#### 1.1 Test en el modelo

Todos los tests para comprobar que el modelo de datos funcione correctamente.
Se utilizan las siguientes variables en los tests:
```
val label = "Tarea test"
val nombreUsuario = "Test"
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

* Se comprueba si se borra una tarea para un usuario. Ademas comprobamos si no existe una tarea o un usuario
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

## III. Repositorios

### Bitbucket
- Enlace a [Bitbucket](https://bitbucket.org/JoseVte/play-todolist)

### Heroku
- Enlace a [Heroku](http://shrouded-refuge-4122.herokuapp.com/tasks)