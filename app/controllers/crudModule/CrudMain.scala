package controllers.crudModule

import play.api._
import play.api.mvc._

class CrudMain(controllers:CrudController[_]*) extends Controller {
  
  def namedControllers = controllers.map(_.name).mkString(",")
  
  def crud(path:String) = Action { request =>
    val parts = path.split('/').toList
    if (parts.size < 2) {
      NotFound("Not found. Not enough arguments.")
    } else {
      val requestedController :: command = parts
      controllers.find(_.name == requestedController).map(controller=> {
        command match {
          case "show"::id::Nil if request.method == "GET" => controller.show(id)
          case "showAll"::Nil if request.method == "GET" => controller.showAll
          case "create"::Nil if request.method == "POST" => controller.create(request)
          case "create"::Nil if request.method == "GET" => controller.createForm
          case "update"::id::Nil if request.method == "POST" => controller.update(id, request)
          case "update"::id::Nil if request.method == "GET" => controller.updateForm(id)
          case "delete"::id::Nil if request.method == "POST" => controller.delete(id)
          case _ => NotFound("Not found")
        }
      }).getOrElse(NotFound(s"Not found. ${parts.head} does not match any controller in ${namedControllers}"))
    }
  }
}
