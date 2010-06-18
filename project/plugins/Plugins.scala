import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
//  val repo = "GH-pages repo" at "http://mpeltonen.github.com/maven/"
//  val idea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1-SNAPSHOT"

   val repo = "Guardian Releases" at "http://nexus.gudev.gnl:8081/nexus/content/repositories/releases"
   // this is just a release of the plugin as of http://github.com/mpeltonen/sbt-idea-plugin/commit/7f7d190d031ba2fce00c27444096eb9520481ed6
   // mpeltonen hasn't got round to publishing a snapshot for it yet.  We can revert to the released version
   // above when he does.
   val ideaPlugin = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1-GUPATCH-1"
}

