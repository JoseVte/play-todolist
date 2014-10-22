package test

import org.specs2.mutable._

import play.api.test._  
import play.api.test.Helpers._

import models.User

class ApplicationSpec extends Specification {
    // Variables de los tests
    val usuarioTest="Test"
    val usuarioIncorrecto="Error"

    "Controlador de la APP - Feature 1" should {
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
    }

    "Controlador de la APP - Feature 2" should {
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
    }
}