package test

import org.specs2.mutable._

import play.api.test._  
import play.api.test.Helpers._

class ApplicationSpec extends Specification {

    "Controlador Application" should {

        "devolver 404 en una ruta incorrecta" in {  
            running(FakeApplication()) {  
                route(FakeRequest(GET, "/incorrecto")) must beNone  
            }
        }

        "pagina por defecto" in {  
            running(FakeApplication()) {
                val Some(home) = route(FakeRequest(GET, "/"))

                status(home) must equalTo(SEE_OTHER)
                redirectLocation(home) must beSome("/tasks")
            }
        }

        "todas las tareas" in {
            running(FakeApplication()) {
                val Some(pag) = route(FakeRequest(GET,"/tasks"))

                status(pag) must equalTo(OK)  
                contentType(pag) must beSome.which(_ == "application/json")  
                contentAsString(pag) must contain ("anonimo") 
            }
        }

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
    }
}