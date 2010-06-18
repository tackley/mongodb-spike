package net.tackley

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import com.mongodb.DB
import net.liftweb.mongodb._
import org.bson.types.ObjectId
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonDSL._


case class User(
        _id: String,
        emailAddress: String,
        password: String,
        name: Option[String],
        lists: List[String]) extends MongoDocument[User] {
  def meta = User
}

object User extends MongoDocumentMeta[User] {
  
}

class FirstMongoHack extends FunSuite with ShouldMatchers {
  MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("localhost", 27017), "test"))

  ignore("insert some users") {
    User.drop
    User(ObjectId.get.toString, "graham@tackley.net", "poo", None, Nil).save
    User(ObjectId.get.toString, "kenlimfc@gmail.com", "wee", Some("Kenneth Lim"), Nil).save
    User(ObjectId.get.toString, "swells@pants.net", "strip_club_tomorrow", Some("Swells"), Nil).save
  }


  ignore("add graham to a list") {
    User.update( ("emailAddress" -> "graham@tackley.net") ~ ("lists" -> ("$ne" -> "system/comment/blockthebastard")), 
      "$push" -> ("lists" -> "system/comment/blockthebastard" ) )
    User.update("emailAddress" -> "swells@pants.net",
      "$push" -> ("lists" -> "subscribe" ) )
    User.update("emailAddress" -> "kenlimfc@gmail.com",
      "$push" -> ("lists" -> "system/comment/awesome" ) )
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
}