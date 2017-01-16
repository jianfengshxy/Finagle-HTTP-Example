package finagle

/**
  * Created by hendisantika on 10/01/17.
  */

import ai.x.play.json.Jsonx
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object PlayJson3 extends App {

  val json: JsValue = Json.parse(
    """
    {
      "name" : "Watership Down",
      "location" : {
        "lat" : 51.235685,
        "long" : -1.309197
      },
      "residents" : [ {
        "name" : "Fiver",
        "age" : 4,
        "role" : null
      }, {
        "name" : "Bigwig",
        "age" : 6,
        "role" : "Owsla"
      } ]
    }
    """)


  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double](min(-90.0) keepAnd max(90.0)) and
      (JsPath \ "long").read[Double](min(-180.0) keepAnd max(180.0))
    )(Location.apply _)

  implicit val residentReads: Reads[Resident] = (
    (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "age").read[Int](min(0) keepAnd max(150)) and
      (JsPath \ "role").readNullable[String]
    )(Resident.apply _)

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "location").read[Location] and
      (JsPath \ "residents").read[Seq[Resident]]
    )(Place.apply _)



  json.validate[Place] match {
    case s: JsSuccess[Place] => {
      val place: Place = s.get
      println(place)
    }
    case e: JsError => {
      // error handling flow
    }
  }
}

case class Location(lat: Double, long: Double)

case class Resident(name: String, age: Int, role: Option[String])

case class Place(name: String, location: Location, residents: Seq[Resident])



