package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Categoria(usuario: String, nombreCategoria: String)

object Categoria {
   val categoria = {
      get[String]("usuario") ~
      get[String]("nombreCategoria") map{
         case usuario~nombreCategoria => Categoria(usuario,nombreCategoria)
      }
   }

   def all(usuario: String): List[Categoria] = DB.withConnection {
      implicit c => SQL("SELECT * FROM categorias WHERE usuario = {usuario}")
         .on("usuario" -> usuario).as(categoria *)
   }

   def create(usuario: String, nombreCategoria: String): Int = DB.withConnection {
      implicit c => SQL("INSERT INTO categorias (usuario,nombreCategoria) VALUES ({usuario},{nombreCategoria})")
         .on("usuario" -> usuario,"nombreCategoria" -> nombreCategoria).executeUpdate()
   }

   def update(usuario: String, nombreAnt: String, nombreNuevo: String): Boolean = (1 == DB.withConnection {
      implicit c => SQL("UPDATE categorias c SET c.nombreCategoria = {nombreNuevo} WHERE c.nombreCategoria = {nombreAnt} AND c.usuario = {usuario}")
         .on("usuario" -> usuario,"nombreAnt" -> nombreAnt,"nombreNuevo" -> nombreNuevo).executeUpdate()
   })

   def delete(usuario: String, nombre: String) = {
      1
   }
}