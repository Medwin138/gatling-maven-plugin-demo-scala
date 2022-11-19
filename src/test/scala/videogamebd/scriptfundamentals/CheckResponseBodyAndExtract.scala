package videogamebd.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt



class CheckResponseBodyAndExtract extends Simulation{

  val httpProtocol=http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value = "application/json")


  //Accertions con Json path https://jsonpath.com/
  val scn=scenario(name = "Check with json Path")
    .exec(http(requestName = "Get all video games - 2nd call")
      .get("/videogame/1")
      .check(jsonPath("$.name").is("Resident Evil 4")))

//Impresion de del fracgmento del Jpath  

    .exec(http(requestName = "Get all video games - 2nd call")
      .get("/videogame")
      .check(jsonPath("$[1].id").saveAs("gameId")))
    .exec{session => println(session);session}

    .exec(http(requestName = "Get specific game")
      .get("/videogame/#{gameId}")
      .check(jsonPath("$.name").is("Gran Turismo 3")))
    .exec{session => println(session("responseBody").as[String]);session}


  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpProtocol)

}
