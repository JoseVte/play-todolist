package test

import org.specs2.mutable._ 

import play.api.test._  
import play.api.test.Helpers._

import org.h2.jdbc.JdbcSQLException

import models.Task
import models.User

class ModelSpec extends Specification {
    //Variables del test
    val label = "Tarea test"
    val nombreUsuario = "Test"

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
                val idTest = Task.create(label,nombreUsuario,null)

                // Borramos la tarea creada
                val ok = Task.delete(nombreUsuario,idTest)
                ok must_== 1
                Task.all(nombreUsuario) must be empty

                // Intentamos volver a borrar la misma tarea
                Task.delete(nombreUsuario,idTest) must_== 0
                // Intentamos volver a borrar una tarea de un usuario que no existe
                Task.delete(null,idTest) must_== 0
                Task.delete("",idTest) must_== 0
                // Intentamos volver a borrar una tarea con otro id
                Task.delete(nombreUsuario,0L) must_== 0
            }
        }
    }  
}