package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class User(id:Long, nombre: String)

object User{
   val user = {
      int("id") ~ str("nombre") map{
         case id~nombre => User(id,nombre)
      }
   }

   def comprobarUsuario(usuario: String): Boolean = {
      val aux = read(usuario)
      return aux!=None
   }

   def all: List[User] = DB.withConnection{
      implicit c => SQL("select * from usuarios").as(user *)
   }

   def read(nombre: String): Option[User] = DB.withConnection{
      implicit c => SQL("select * from usuarios where nombre = {nombre}").on("nombre" -> nombre).as(user.singleOpt)
   }

   def create(nombre: String): Option[Long] = DB.withConnection{
      implicit c => SQL("insert into usuarios(nombre) values ({nombre})").on("nombre" -> nombre).executeInsert()
   }

   def update(nombreAnt: String, nombreNuevo: String): Boolean = {
      val result: Int = DB.withConnection{
         implicit c => SQL("update usuarios u set u.nombre={nombreNuevo} where u.nombre={nombreAnt}").on("nombreAnt" -> nombreAnt,"nombreNuevo" -> nombreNuevo).executeUpdate()
      }
      return result==1
   }

   def delete(nombre: String): Boolean = {
      val result: Int = DB.withConnection{
         implicit c => SQL("delete from usuarios u where u.nombre={nombre}").on("nombre" -> nombre).executeUpdate()
      }
      return result==1
   }
}