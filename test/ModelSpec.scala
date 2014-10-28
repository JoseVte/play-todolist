package test

import org.specs2.mutable._ 

import play.api.test._  
import play.api.test.Helpers._

import org.h2.jdbc.JdbcSQLException

import models.Task
import models.User
import models.Categoria

import java.util.Date
import java.text.SimpleDateFormat

class ModelSpec extends Specification {
    //Variables del test
    val label = "Tarea test"
    val nombreUsuario = "Test"
    val nombreNuevoUsuario = "Nuevo test"
    val fecha:Option[Date] = Some(new Date)
    val dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val nombreCategoria = "CategoriaTest"
    val nombreNuevoCategoria = "NuevaCategoriaTest"

    "Modelo de Task" should {
        "crear tarea" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                // LLamamos al modelo para crear una tarea
                // Comprobamos que se haya creado correctamente

                User.crearUser(nombreUsuario)
                val idTest = Task.create(label,nombreUsuario,null)
                idTest must be_>(0L)

                // Probamos a crear una tarea sin usuario null
                Task.create(label,null,null) must throwA[JdbcSQLException]
                // Probamos a crear una tarea sin usuario ""
                Task.create(label,"",null) must throwA[JdbcSQLException]
            }
        }

        "todas las tareas" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                // Primero creamos una tarea de prueba
                User.crearUser(nombreUsuario)

                // Comprobamos que la lista inicial esta vacia
                Task.all(nombreUsuario) must be empty

                val idTest = Task.create(label,nombreUsuario,null)

                // Comprobamos todas las tareas
                val lista = Task.all(nombreUsuario)
                lista must be have size(1)
                lista(0).id must_== idTest
                lista(0).label must_== label

                // Probamos a listar una tarea sin usuario ""
                Task.all("") must be empty
            }
        }

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
    }

    "Modelo de User" should {
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
    }

    "Modelo de Task con fechas" should {
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
    }

    "Modelo de Categoria" should {
        "crear categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                cat must be_>(0)

                // Probamos a crear una cateogria sin usuario null
                Categoria.create(null,nombreCategoria) must throwA[JdbcSQLException]
            }
        }

        "mostrar categorias" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val cats = Categoria.all(nombreUsuario)
                cats must be have size(1)
                cats(0).usuario must_== nombreUsuario
                cats(0).nombreCategoria must_== nombreCategoria

                // Probamos a listar una tarea sin usuario ""
                Categoria.all("") must be empty
            }
        }

        "modificar categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val ok = Categoria.update(nombreUsuario,nombreCategoria,nombreNuevoCategoria)
                ok must beTrue

                val error = Categoria.update(nombreUsuario,nombreCategoria,nombreNuevoCategoria)
                error must beFalse
            }
        }

        "borrar categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val ok = Categoria.delete(nombreUsuario,nombreCategoria)
                ok must beTrue

                val error = Categoria.delete(nombreUsuario,nombreCategoria)
                error must beFalse
            }
        }
        
        "añadir tareas a categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val idTest = Task.create(label,nombreUsuario,nombreCategoria,null)
                idTest must be_>(0L)
            }
        }

        "mostrar todas las tareas de una categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val idTest = Task.create(label,nombreUsuario,nombreCategoria,null)
                idTest must be_>(0L)
            }
        }

        "quitar tareas de una categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                User.crearUser(nombreUsuario)
                val cat = Categoria.create(nombreUsuario,nombreCategoria)

                val idTest = Task.create(label,nombreUsuario,nombreCategoria,null)
                idTest must be_>(0L)
            }
        }
    }
}