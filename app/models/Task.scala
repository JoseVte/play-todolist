package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {
	val task = {
		get[Long]("id") ~
		get[String]("label") map{
			case id~label => Task(id,label)
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

	def create(label: String, usuario: String) : Long = {
		var id: Long = 0
		DB.withConnection{
			implicit c => 
				id = SQL("insert into task(label,usuario) values ({label},{usuario})")
				.on("label" -> label, "usuario" -> usuario).executeInsert().get
		}
		return id
	}

	def delete(usuario:String, id: Long) : Int = {
		var numRows = 0
		DB.withConnection{
			implicit c => 
				numRows = SQL("delete from task where id = {id} and usuario = {usuario}").on("id" -> id,"usuario" -> usuario).executeUpdate()
		}
		return numRows
	}
}