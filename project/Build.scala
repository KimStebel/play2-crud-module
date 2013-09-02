import sbt._
import Keys._
import PlayProject._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "crud-module"
    val appVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
