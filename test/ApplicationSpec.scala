package test

import org.specs2.mutable._

import play.api.test._  
import play.api.test.Helpers._

import models.User
import models.Categoria
import models.Task

import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Timestamp

class ApplicationSpec extends Specification {
    // Variables de los tests
    val usuarioTest="Test"
    val usuarioIncorrecto="Error"
    val fecha="24-10-2014"
    val fechaIncorrecta="24-10"
    val categoriaTest="CategoriaTest"
    val categoriaNuevaTest="CategoriaNuevaTest"

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
                contentAsString(form) must contain ("\"label\":\"Test\"")

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

        "una tarea concreta para un usuario" in {
            running(FakeApplication()) {
                // Se comprueba en otros test esta funcionalidad
                User.crearUser(usuarioTest)
                val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody(("label","Test")))

                val Some(pag) = route(FakeRequest(GET,"/"+usuarioTest+"/tasks/5"))

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

        "borrado de una tarea de un usuario" in {
            running(FakeApplication()) {
                // Se comprueba en otros test esta funcionalidad
                User.crearUser(usuarioTest)
                val Some(form) = route(FakeRequest(POST,"/"+usuarioTest+"/tasks").withFormUrlEncodedBody(("label","Test")))

                val Some(del) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/5"))

                status(del) must equalTo(OK)
                contentType(del) must beSome.which(_ == "text/plain")
                contentAsString(del) must contain("5")

                // Volvemos a borrar la tarea y deberia dar error
                val Some(error) = route(FakeRequest(DELETE,"/"+usuarioTest+"/tasks/1"))
                status(error) must equalTo(NOT_FOUND)
                contentType(error) must beSome.which(_ == "text/html")
                contentAsString(error) must contain("404")
            }
        }
    }

    "Controlador de la APP - Feature 3" should {
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
    }

    "Controlador de la APP - Feature TDD" should {
        "todas las categorias de un usuario" in {
            running(FakeApplication()) {
                User.crearUser(usuarioTest)
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

        "crear una categoria de un usuario" in {
            running(FakeApplication()) {
                User.crearUser(usuarioTest)
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

        "actualizar nombre de una categoria" in {
            running(FakeApplication()) {
                User.crearUser(usuarioTest)
                Categoria.create(usuarioTest,categoriaTest)
                Task.create("Test",usuarioTest,categoriaTest,null)

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
                    .withFormUrlEncodedBody(("categoriaAnt",categoriaTest),("categoriaNueva",categoriaNuevaTest)))

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
    }
}