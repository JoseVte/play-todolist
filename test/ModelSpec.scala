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

    "Models" should {
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
    }  
}