package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Categoria(user: String, nombre: String)

object Categoria {

   def all(usuario: String) = {
      List(1)
   }

   def create(usuario: String, nombre: String): Int = DB.withConnection {
      implicit c => SQL("INSERT INTO categorias (usuario,nombreCategoria) VALUES ({usuario},{nombre})")
         .on("usuario" -> usuario,"nombre" -> nombre).executeUpdate()
   }

   def update(usuario: String, nombreAnt: String, nombreNuevo: String) = {
      1
   }

   def delete(usuario: String, nombre: String) = {
      1
   }
}