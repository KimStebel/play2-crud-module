package controllers.crudModule

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.templates.Html
import play.api.mvc.Results

trait FormHandler[A] {
  def parseUpdate(request:Request[_]):A
  def parseCreate(request:Request[_]):A
  def show(a:A):Html
  def showAll(l:List[A]):Html
  def createForm:Html
  def updateForm(id:String, a:A):Html
}

case class Person(name:String, age:Int)

object PersonFormHandler extends FormHandler[Person] {
  
  import play.api.data._
  import play.api.data.Forms._

  val form = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )
    (Person.apply)
    (Person.unapply)
  )
  val fields = form.mapping.key
  
  def createForm:Html = views.html.CreateForm(form)
  def updateForm(id:String, p:Person):Html = views.html.UpdateForm(form)
  
  
  def parseUpdate(request:Request[_]):Person = form.bindFromRequest()(request).get
  def parseCreate(request:Request[_]):Person = parseUpdate(request)
  
  def show(p:Person):Html = Html(p.toString)
  def showAll(l:List[Person]):Html = l.map(show).reduce(_+_)
}

abstract class CrudController[A](implicit dbm:DbManager[A], fh:FormHandler[A]) extends Controller {
  val name:String
  private def tryOr500(r: =>Result):Result = try r catch { case _:Exception => Results.InternalServerError }
  def show(id:String):Result = Ok(fh.show(dbm.read(id)))
  def create(request:Request[_]):Result = tryOr500 {
    dbm.create(fh.parseCreate(request))
    Ok
  }
  def createForm:Result = {
    Ok(fh.createForm)
  }
  def update(id:String, request:Request[_]):Result = tryOr500 {
    dbm.update(id, fh.parseUpdate(request))
    Ok
  }
  def updateForm(id:String):Result = {
    val a = dbm.read(id)
    Ok(fh.updateForm(id, a))
  }
  def delete(id:String):Result = {
    dbm.delete(id)
    Ok
  }
  def showAll:Result = {
    val l = dbm.getAll
    Ok(fh.showAll(l))
  }
}
