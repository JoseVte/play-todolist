package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.json.Json

import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat

import models.Task
import models.User

object Application extends Controller {

   val parseDate: String = "^([0-9]{1,2}-[0-9]{1,2}-[0-9]{4})$"
   val formatoParse: String = "dd-MM-yyyy"

   val taskForm = Form(
      mapping(
         "id" -> ignored(0L),
         "label" -> nonEmptyText,
         "fechaFin" -> optional(date(formatoParse))
      )(Task.apply)(Task.unapply)
   )

   implicit val taskWrites = new Writes[Task] {
      def writes(task: Task) = Json.obj(
         "id" -> task.id,
         "label" -> task.label,
         "fechaFin" -> task.fechaFin)
   }

   def errores(mensajeError: String) : String = {
      var error: String = "<html><head><title>- ooops! -</title><style>body { background:#0000aa;color:#ffffff;font-family:courier;font-size:12pt;text-align:center;margin:100px;}blink {color:yellow;}.neg {background:#fff;color:#0000aa;padding:2px 8px;font-weight:bold;}p {margin:30px 100px;text-align:left;font-size: 20;}a,a:hover {color:inherit;font:inherit;}</style></head><body><h1><span class=\"neg\">"
      error+=mensajeError
      error+="</span></h1><p><br>Usted puede esperar y ver si vuelve a estar disponible, o puede reiniciar su PC.</p><p>* Env&iacute;enos un e-mail para notificar esto e int&eacute;ntelo mas tarde.<br />* Pulse CTRL+ALT+SUPR para reiniciar su PC. Usted perder&aacute; toda la informaci&oacute;n no guardada en cualquier programa que este ejecutando.</p>Pulse cualquier link para continuar<blink>_</blink><div class=\"menu\"><a href=\"\">Recargar</a> | <a href=\"http://www.google.es\">Google</a> |</div></body></html>";
      return error;
   }

   def index = Action {
      Redirect(routes.Application.tasks("anonimo"))
   }

   def tasks(usuario: String) = Action {
      if(User.comprobarUsuario(usuario)){
         val json = Json.toJson(Map(usuario -> Json.toJson(Task.all(usuario))))
         Ok(json)
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def readTask(usuario: String, id: Long) = Action {
      if(User.comprobarUsuario(usuario)){
         Task.read(usuario,id) match {
            case Some(task) => 
               val json = Json.toJson(task)
               Ok(json)
            case None => NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def tasksFinalizadas(usuario: String, fecha: String) = Action {
      if(User.comprobarUsuario(usuario)){
         val formatoURI = new SimpleDateFormat(formatoParse)
         var fechaParse = new Date()
         if(fecha != null && fecha.matches(parseDate)){
            fechaParse = formatoURI.parse(fecha)
            val json = Json.toJson(Task.all(usuario,fechaParse))
            Ok(json)
         }else if(fecha == null){
            fechaParse = Calendar.getInstance().getTime()
            val json = Json.toJson(Task.all(usuario,fechaParse))
            Ok(json)
         } else {
            BadRequest(errores("Error 400: La fecha ("+fecha+") no esta en el formato correcto")).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def newTask(usuario: String) = Action { implicit request =>
      taskForm.bindFromRequest.fold(
         errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
         task => {
            if(User.comprobarUsuario(usuario)){
               val id = Task.create(task.label,usuario,task.fechaFin)
               val json = Json.toJson(Map(usuario -> Json.toJson(new Task(id,task.label,task.fechaFin))))
               Created(json)
            } else {
               NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
            }
         }
      )
   }

   def deleteTask(usuario: String, id: Long) = Action {
      if(User.comprobarUsuario(usuario)){
         val resultado : Int = Task.delete(usuario,id)
         if(resultado == 1){
            Ok("Tarea "+id+" del usuario "+usuario+" borrada correctamente")
         } else {
            NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe para el usuario "+usuario)).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def deleteTaskDate(usuario: String, fecha: String) = Action {
      if(User.comprobarUsuario(usuario)){
         val formatoURI = new SimpleDateFormat(formatoParse)
         if(fecha != null && fecha.matches(parseDate)){
            val fechaParse : Date = formatoURI.parse(fecha)
            val numRows : Int = Task.deleteDate(usuario,fechaParse)
            Ok("Se han borrado "+numRows+" de tareas del usuario "+usuario+" hasta la fecha "+fecha)
         } else {
            BadRequest(errores("Error 400: La fecha ("+fecha+") no esta en el formato correcto")).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def categorias(usuario: String) = Action {
      Ok(Json.toJson("Categoria1"))
   }
}