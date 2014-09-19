package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._

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
  	Redirect(routes.Application.tasks)
	}

	def tasks = Action {
		Ok(Json.toJson(Task.all()))
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
		Task.delete(id)
		Redirect(routes.Application.tasks)
	}
}