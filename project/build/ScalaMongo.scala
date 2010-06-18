import java.util.Date
import java.util.jar.Attributes
import sbt._

class ScalaMongoProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  
  val localMaven = "Local Maven Repo" at "file://" + Path.userHome + "/.m2/repository"


  val SLF4J_VERSION = "1.6.0"

  val slf4jApi = "org.slf4j" % "slf4j-api" % SLF4J_VERSION
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.21"

  val scalaTest = "org.scalatest" % "scalatest" % "1.0" % "test"

  val liftMongo = "net.liftweb" % "lift-mongodb" % "2.0-RC1" withSources()
  val liftCommon = "net.liftweb" % "lift-common" % "2.0-RC1" withSources()
  val liftJson = "net.liftweb" % "lift-json" % "2.0-RC1" withSources()

  // explicitly specifying this to get the sources
  val mongoJavaDriver = "org.mongodb" % "mongo-java-driver" % "2.0rc4" withSources()
  
  override def ivyUpdateLogging = UpdateLogging.Full
}
