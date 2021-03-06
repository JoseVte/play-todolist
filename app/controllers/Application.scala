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
import models.Categoria

object Application extends Controller {

   val parseDate: String = "^([0-9]{1,2}-[0-9]{1,2}-[0-9]{4})$"
   val formatoParse: String = "dd-MM-yyyy"

   val taskForm = Form(
      mapping(
         "id" -> ignored(0L),
         "label" -> nonEmptyText,
         "fechaFin" -> optional(date(formatoParse)),
         "categoria" -> optional(text)
      )(Task.apply)(Task.unapply)
   )

   val categoriaForm = Form(
      "categoria" -> nonEmptyText
   )

   val categoriaUpdateForm = Form(
      tuple(
         "categoriaAnt" -> nonEmptyText,
         "categoriaNueva" -> nonEmptyText
      )
   )

   val taskUpdateForm = Form(
      tuple(
         "id" -> number,
         "categoriaNueva" -> text
      )
   )

   implicit val taskWrites = new Writes[Task] {
      def writes(task: Task) = Json.obj(
         "id" -> task.id,
         "label" -> task.label,
         "fechaFin" -> task.fechaFin,
         "categoria" -> task.categoria)
   }

   implicit val categoriaWrites = new Writes[Categoria] {
      def writes(categoria: Categoria) = Json.obj(
         "nombreCategoria" -> categoria.nombreCategoria)
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

   def tasksPorCategoria(usuario: String, categoria: String) = Action{
      if(User.comprobarUsuario(usuario)){
         if(Categoria.comprobarCategoria(usuario,categoria)){
            Ok(Json.toJson(Task.all(usuario,categoria)))
         } else {
            NotFound(errores("Error 404: La categoria "+categoria+" no existe")).as("text/html")
         }
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
               if(Categoria.comprobarCategoria(usuario,task.categoria)){
                  val id = Task.create(task.label,usuario,task.fechaFin,task.categoria)
                  val json = Json.toJson(Map(usuario -> Json.toJson(Task.read(usuario,id))))
                  Created(json)
               } else {
                  NotFound(errores("Error 404: La categoria "+task.categoria.get+" no existe para el usuario "+usuario)).as("text/html")
               }
            } else {
               NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
            }
         }
      )
   }

   def updateCategoriaTask(usuario: String) = Action { implicit request =>
      taskUpdateForm.bindFromRequest.fold(
         errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
         update => {
            if(User.comprobarUsuario(usuario)){
               if(Categoria.comprobarCategoria(usuario,update._2)){
                  if(Task.updateCategoria(usuario,update._2,update._1)){
                     Ok("La tarea "+update._1+" del usuario "+usuario+" se ha trasladado a la categoria "+update._2+" correctamente")
                  } else {
                     NotFound(errores("Error 404: La tarea con el id "+update._1+" no existe para el usuario "+usuario)).as("text/html")
                  }
               } else {
                  NotFound(errores("Error 404: La categoria "+update._2+" no existe para el usuario "+usuario)).as("text/html")
               }
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

   def deleteTaskCategoria(usuario: String, categoria: String) = Action {
      if(User.comprobarUsuario(usuario)){
         if(Categoria.comprobarCategoria(usuario,categoria)){
            Ok("Todas las tareas de la categoria "+categoria+" han sido borradas. Total: "+Task.deleteCategoria(usuario,categoria))
         } else {
            NotFound(errores("Error 404: La categoria "+categoria+" no existe para el usuario "+usuario)).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def deleteCategoriaTask(usuario: String, id: Long) = Action {
      if(User.comprobarUsuario(usuario)){
         if(None != Task.read(usuario,id)){
            Task.updateCategoria(usuario,null,id)
            Ok("La tarea "+id+" se le ha borrado la categoria correctamente")
         } else {
            NotFound(errores("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def categorias(usuario: String) = Action {
      if(User.comprobarUsuario(usuario)){
         val json = Json.toJson(Map(usuario -> Json.toJson(Categoria.all(usuario))))
         Ok(json)
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }

   def newCategoria(usuario: String) = Action { implicit request =>
      categoriaForm.bindFromRequest.fold(
         errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
         categoria => {
            if(User.comprobarUsuario(usuario)){
               if(!Categoria.comprobarCategoria(usuario,categoria)){
                  Categoria.create(usuario,categoria)
                  val json = Json.toJson(Map(usuario -> Json.toJson(new Categoria(usuario,categoria))))
                  Created(json)
               } else {
                  BadRequest(errores("Error 400: La categoria "+categoria+" ya existe")).as("text/html")
               }
            } else {
               NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
            }
         }
      )
   }

   def updateCategoria(usuario: String) = Action { implicit request =>
      categoriaUpdateForm.bindFromRequest.fold(
         errors => BadRequest(errores("Error 400: El formulario POST esta mal definido o faltan campos")).as("text/html"),
         form => {
            if(User.comprobarUsuario(usuario)){
               if(!Categoria.comprobarCategoria(usuario,form._2)){
                  if(Categoria.update(usuario,form._1,form._2)){
                     Ok("La categoria "+form._1+" se ha modificado correctamente a "+form._2)
                  } else {
                     NotFound(errores("Error 404: La categoria "+form._1+" no se ha podido modificar porque no se encuentra")).as("text/html")
                  }
               } else {
                  BadRequest(errores("Error 400: La categoria "+form._1+" no se ha podido modificar porque el nuevo nombre ya existe")).as("text/html")
               }
            } else {
               NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
            }
         }
      )
   }

   def deleteCategoria(usuario: String, categoria: String) = Action {
      if(User.comprobarUsuario(usuario)){
         if(Categoria.delete(usuario,categoria)){
            Ok("La categoria "+categoria+" se ha borrado correctamente")
         } else {
            NotFound(errores("Error 404: La categoria "+categoria+" no existe")).as("text/html")
         }
      } else {
         NotFound(errores("Error 404: El usuario "+usuario+" no existe")).as("text/html")
      }
   }
}