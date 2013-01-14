package controllers.crudModule

trait DbManager[A] {
  def read(id:String):A
  def create(a:A)
  def update(id:String, a:A)
  def delete(id:String)
  def getAll:List[A]
}

class InMemoryDbManager[A] extends DbManager[A] {
  var m = Map[String,A]()
  var currentId = 0
  def read(id:String):A = m(id)
  def create(a:A) = {
    m += currentId.toString -> a
    currentId += 1
  } 
  def update(id:String, a:A) {
    m += id-> a
  }
  def delete(id:String) {
    m -= id
  }
  def getAll:List[A] = m.toList.map(_._2)
}
