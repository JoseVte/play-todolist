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

   def create(usuario: String, nombre: String) = {
      Some(1L)
   }

   def update(usuario: String, nombreAnt: String, nombreNuevo: String) = {
      1
   }

   def delete(usuario: String, nombre: String) = {
      1
   }
}