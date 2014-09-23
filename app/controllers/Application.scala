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

  def readTask(usuario: String, id: Long) = Action {
    try{
      val json = Json.toJson(Task.read(usuario,id))
      Ok(json)
    } catch {
      case e: NoSuchElementException => NotFound("Error 404: La tarea con el identificador "+id+" no existe en el usuario "+usuario)
    }
  }

	def newTask(usuario: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest,
      label => {
        try{
          val id = Task.create(label,usuario)
          val json = Json.toJson(Map(usuario -> Json.toJson(new Task(id,label))))
          Created(json)
        } catch {
          case _ => NotFound("Error 404: El usuario "+usuario+" no existe")
        }
      }
    )
  }

	def deleteTask(usuario: String, id: Long) = Action {
		val resultado : Int = Task.delete(usuario,id)
		if(resultado == 1){
      Ok("Tarea "+id+" borrada correctamente")
    } else {
      NotFound("Error 404: La tarea con el identificador "+id+" no existe para el usuario "+usuario)
    }
	}
}