package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

import java.util.Date

case class Task(id: Long, label: String, fechaFin: Option[Date], categoria: Option[String])

object Task {
   val task = {
      get[Long]("id") ~
      get[String]("label") ~
      get[Option[Date]]("fechaFin") ~
      get[Option[String]]("categoria") map{
         case id~label~fechaFin~categoria => Task(id,label,fechaFin,categoria)
      }
   }

   def all(usuario: String): List[Task] = DB.withConnection{
      implicit c => SQL("select * from task where usuario = {usuario}").on("usuario" -> usuario).as(task *)
   }

   def all(usuario: String, fecha: Date): List[Task] = DB.withConnection{
      implicit c => SQL("select * from task where usuario = {usuario} and fechaFin <= {fecha}")
      .on("usuario" -> usuario,"fecha" -> fecha).as(task *)
   }

   def all(usuario: String, categoria: String): List[Task] = DB.withConnection {
      implicit c => SQL("select * from task where usuario = {usuario} and categoria = {categoria}")
      .on("usuario" -> usuario, "categoria" -> categoria).as(task *)
   }

   def read(usuario: String, id: Long): Option[Task] = DB.withConnection{ 
      implicit c =>
         SQL("select * from task where id = {id} and usuario = {usuario}")
         .on("id" -> id, "usuario" -> usuario).as(task.singleOpt)
   }

   def create(label: String, usuario: String, fechaFin: Option[Date] = None, categoria: Option[String] = None) : Long = DB.withConnection{
      implicit c => SQL("insert into task(label,usuario,fechaFin, categoria) values ({label},{usuario},{fechaFin},{categoria})")
         .on("label" -> label, "usuario" -> usuario, "categoria" -> categoria, "fechaFin" -> fechaFin).executeInsert().get
   }

   def modificarCategoria(usuario: String, nuevaCategoria: String, id: Long) : Boolean = (1 == DB.withConnection{
      implicit c => SQL("update task set categoria = {categoria} where usuario = {usuario} and id = {id}")
         .on("usuario" -> usuario, "categoria" -> nuevaCategoria, "id" -> id).executeUpdate()
   })

   def delete(usuario: String, id: Long) : Int = DB.withConnection{
      implicit c => SQL("delete from task where id = {id} and usuario = {usuario}")
         .on("id" -> id,"usuario" -> usuario).executeUpdate()
   }

   def deleteDate(usuario: String, fecha: Date) : Int = DB.withConnection{
      implicit c => SQL("delete from task where usuario = {usuario} and fechaFin < {fecha}")
         .on("usuario" -> usuario,"fecha" -> fecha).executeUpdate()
   }

   def deleteCategoria(usuario: String, categoria: String) : Int = DB.withConnection{
      implicit c => SQL("delete from task where usuario = {usuario} and categoria = {categoria}")
         .on("usuario" -> usuario, "categoria" -> categoria).executeUpdate()
   }
}