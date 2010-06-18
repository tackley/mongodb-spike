package net.tackley

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import net.liftweb.mongodb._
import org.bson.types.ObjectId
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonDSL._
import com.mongodb.DB.WriteConcern
import com.mongodb.{MongoException, DB}


case class User(
        _id: String,
        emailAddress: String,
        password: String,
        name: Option[String],
        var lists: List[String]) extends MongoDocument[User] {
  def meta = User
}

object User extends MongoDocumentMeta[User] {
  
}

/*
case class MongoAddress(host: MongoHost, name: String) {
  def db = host.mongo.getDB(name)

  override def toString = host.host+":"+host.port+"/"+name
}

 */

case class StrictMongoAddress(override val host: MongoHost, override val name: String)
        extends MongoAddress(host, name) {
  override def db = strict(host.mongo.getDB(name))

  def strict(db: DB) = { db.setWriteConcern(WriteConcern.STRICT); db }
}

class FirstMongoHack extends FunSuite with ShouldMatchers {
  MongoDB.defineDb(DefaultMongoIdentifier, StrictMongoAddress(MongoHost("localhost", 27017), "test"))

  ignore("insert some users") {
    User.drop
    User(ObjectId.get.toString, "graham@tackley.net", "poo", None, Nil).save
    User(ObjectId.get.toString, "kenlimfc@gmail.com", "wee", Some("Kenneth Lim"), Nil).save
    User(ObjectId.get.toString, "swells@pants.net", "strip_club_tomorrow", Some("Swells"), Nil).save
  }


  test("add graham to a list") {
    User.update(
      "emailAddress" -> "graham@tackley.net",
      "$addToSet" -> ("lists" -> "should_only_be_added_once" ) )
    
//    User.update("emailAddress" -> "swells@pants.net",
//      "$push" -> ("lists" -> "subscribe" ) )
//    User.update("emailAddress" -> "kenlimfc@gmail.com",
//      "$push" -> ("lists" -> "system/comment/awesome" ) )
  }

  ignore("rename an entry in a list") {
    User.update(
      "lists" -> "system/comment/blockthebastard",
      "$set" -> ( "lists.$" -> "nocomment" )
      )
  }

  def validateUser(email: String, password: String): Option[User] =
    User.find( ("emailAddress" -> email) ~ ("password" -> password) )

  test("authorise") {
    println(validateUser("graham@tackley.net", "badpassword"))
    println(validateUser("graham@tackley.net", "poo"))

  }

  test("tell me the subscribers") {
    User.findAll("lists" -> "subscribe").foreach(println(_))
  }

  ignore("edit a specific user") {
    val gt = User.find("emailAddress" -> "graham@tackley.net").get
    gt.lists = List("new", "stuff")
    gt.save
  }

  test("try to create dodgy duplicate") {
    // this test only passes thanks to the "StrictMongoAddress" above
    // i.e. only when the DB (or Collection) object has a WriteConcern of STRICT
    User.ensureIndex("emailAddress" -> 1, true)
    evaluating {
      User(ObjectId.get.toString, "graham2@tackley.net", "first", None, Nil).save
      User(ObjectId.get.toString, "graham2@tackley.net", "second", None, Nil).save
    } should produce [MongoException.DuplicateKey]
  }
}