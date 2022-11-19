package videogamebd.scriptfundamentals

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import jdk.jfr.internal.event.EventConfiguration.duration

import scala.concurrent.duration.DurationInt

class CheckresponseCode extends Simulation{


  val httpProtocol = http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value="application/json")

  val scn = scenario(name = "Video game DB - 3 calls")


    //Resultado esperado ststus 200
    .exec(http(requestName = "Get all video games - 1st call")
      .get("/videogame")
      .check(status.is(200)))
      .pause(duration=5)

    //Resultado esperado entre 200 y 210
    .exec(http(requestName = "Get specific game ")
      .get("/videogame/1")
      .check(status.in(200 to(210))))
      .pause(1,10)


    //Resultados no esperados 404 y 500
    .exec(http(requestName = "Get all video games - 2nd call")
      .get("/videogame")
      .check(status.not(404),status.not(500)))
      .pause(3000.milliseconds)



  setUp(scn.inject(atOnceUsers(users = 100))).protocols(httpProtocol)
}
