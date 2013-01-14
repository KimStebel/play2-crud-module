package controllers.crudModule

import play.api._
import play.api.mvc._

class CrudMain(controllers:CrudController[_]*) extends Controller {
  
  def crud(path:String) = Action { request =>
    val parts = path.split('/').toList
    if (parts.size < 2) {
      Results.NotFound("Not found")
    } else {
      controllers.find(_.name == parts.head).map(controller=> {
        parts.tail match {
          case "show"::id::Nil if request.method == "GET" => controller.show(id)
          case "showAll"::Nil if request.method == "GET" => controller.showAll
          case "create"::Nil if request.method == "POST" => controller.create(request)
          case "create"::Nil if request.method == "GET" => controller.createForm
          case "update"::id::Nil if request.method == "POST" => controller.update(id, request)
          case "update"::id::Nil if request.method == "GET" => controller.updateForm(id)
          case "delete"::id::Nil if request.method == "POST" => controller.delete(id)
          case _ => Results.NotFound("Not found")
        }
      }).getOrElse(Results.NotFound("Not found"))
    }
  }
}


object PersonController extends CrudController[Person]()(new InMemoryDbManager[Person], PersonFormHandler) {
  override val name = "person"
}