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
                val Some(home) = route(FakeRequest(GET, "/tasks"))

                status(home) must equalTo(OK)  
                contentType(home) must beSome.which(_ == "application/json")  
                contentAsString(home) must contain ("anonimo")  
            }
        }
    }
}