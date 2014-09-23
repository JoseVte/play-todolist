package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.json.Json

import models.Task

object Application extends Controller {

	val taskForm = Form(
  	"label" -> nonEmptyText
	)

  implicit val taskWrites = new Writes[Task] {
    def writes(task: Task) = Json.obj(
      "id" -> task.id,
      "label" -> task.label)
  }

	def index = Action {
  	Redirect(routes.Application.tasks("anonimo"))
	}

	def tasks(usuario: String) = Action {
    val json = Json.toJson(Map(usuario -> Json.toJson(Task.all(usuario))))
		Ok(json)
	}

   def readTask(id: Long) = Action {
      try{
         val json = Json.toJson(Task.read(id))
         Ok(json)
      } catch {
         case _ => NotFound("Error 404: La tarea con el identificador "+id+" no existe")
      }
   }

	def newTask = Action { implicit request =>
      taskForm.bindFromRequest.fold(
         errors => BadRequest("Error 500: No se ha podido crear la nueva tarea"),
         label => {
            Task.create(label)
            val json = Json.toJson(label)
            Created(json)
         }
      )
   }

	def deleteTask(id: Long) = Action {
		val resultado : Int = Task.delete(id)
		if(resultado == 1){
         Ok("Tarea "+id+" borrada correctamente")
      } else {
         NotFound("Error 404: La tarea con el identificador "+id+" no existe")
      }
	}
}