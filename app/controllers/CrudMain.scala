package controllers

import play.api._
import play.api.mvc._

object CrudMain extends Controller {
  
  def crud(path:String) = Action {
    Ok(path)
  }
  
}
