package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.json.Json

import java.util.Date
import java.text.SimpleDateFormat

import models.Task

object Application extends Controller {

	val taskForm = Form(
    mapping(
      "id" -> ignored(0L),
      "label" -> nonEmptyText,
      "fechaFin" -> optional(date("dd/mm/yyyy"))
    )(Task.apply)(Task.unapply)
	)

  implicit val taskWrites = new Writes[Task] {
    def writes(task: Task) = Json.obj(
      "id" -> task.id,
      "label" -> task.label,
      "fechaFin" -> task.fechaFin)
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

  def tasksFinalizadas(usuario: String, fecha: String) = TODO

	def newTask(usuario: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest,
      task => {
        try{
          val id = Task.create(task.label,usuario,task.fechaFin)
          val json = Json.toJson(Map(usuario -> Json.toJson(new Task(id,task.label,task.fechaFin))))
          Created(json)
        } catch {
          case _ : Throwable => NotFound("Error 404: El usuario "+usuario+" no existe")
        }
      }
    )
  }

	def deleteTask(usuario: String, id: Long) = Action {
		val resultado : Int = Task.delete(usuario,id)
		if(resultado == 1){
      Ok("Tarea "+id+" del usuario "+usuario+" borrada correctamente")
    } else {
      NotFound("Error 404: La tarea con el identificador "+id+" no existe para el usuario "+usuario)
    }
	}

  def deleteTaskDate(usuario: String, fecha: String) = Action {
    val formatoURI = new SimpleDateFormat("dd-MM-yyyy")
    val fechaParse : Date = formatoURI.parse(fecha)
    val numRows : Int = Task.deleteDate(usuario,fechaParse)
    Ok("Se han borrado "+numRows+" de tareas del usuario "+usuario+" hasta la fecha "+fecha)
  }
}