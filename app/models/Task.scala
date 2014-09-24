package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

import java.util.Date

case class Task(id: Long, label: String, fechaFin: Option[Date])

object Task {
	val task = {
		get[Long]("id") ~
		get[String]("label") ~
		get[Option[Date]]("fechaFin") map{
			case id~label~fechaFin => Task(id,label,fechaFin)
		}
	}

	def all(usuario: String): List[Task] = DB.withConnection{
		implicit c => SQL("select * from task where usuario = {usuario}").on("usuario" -> usuario).as(task *)
	}

	def read(usuario: String, id: Long): Task = DB.withConnection{ 
		implicit c =>
			SQL("select * from task where id = {id} and usuario = {usuario}")
			.on("id" -> id, "usuario" -> usuario).as(task *).head
	}

	def create(label: String, usuario: String,fechaFin: Option[Date]) : Long = {
		var idNuevo: Long = 0
		var aux: Date = new Date
		if(!fechaFin.isEmpty){
			aux = fechaFin.get
		}
		DB.withConnection{
			implicit c => 
				idNuevo = SQL("insert into task(label,usuario,fechaFin) values ({label},{usuario},{fechaFin})")
				.on("label" -> label, "usuario" -> usuario,"fechaFin" -> aux).executeInsert().get
		}
		return idNuevo
	}

	def delete(usuario: String, id: Long) : Int = {
		var numRows = 0
		DB.withConnection{
			implicit c => 
				numRows = SQL("delete from task where id = {id} and usuario = {usuario}").on("id" -> id,"usuario" -> usuario).executeUpdate()
		}
		return numRows
	}

	def deleteDate(usuario: String, fecha: Date) : Int = {
		var numRows = 0
		DB.withConnection{
			implicit c =>
				numRows = SQL("delete from task where usuario = {usuario} and fechaFin < {fecha}").on("usuario" -> usuario,"fecha" -> fecha).executeUpdate()
		}
		return numRows
	}
}