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
    }
}