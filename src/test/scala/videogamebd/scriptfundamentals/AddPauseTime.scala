package videogamebd.scriptfundamentals

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import jdk.jfr.internal.event.EventConfiguration.duration

import scala.concurrent.duration.DurationInt

class AddPauseTime extends Simulation{


  val httpProtocol = http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value="application/json")

  val scn = scenario(name = "Video game DB - 3 calls")


    //Pausa 5 seg
    .exec(http(requestName = "Get all video games - 1st call")
    .get("/videogame"))
    .pause(duration=5)

    //pausa de 1  a 10 seg ramdon
    .exec(http(requestName = "Get specific game ")
    .get("/videogame/1"))
    .pause(1,10)


    //pausa 3000 milisegundos
    .exec(http(requestName = "Get all video games - 2nd call")
    .get("/videogame"))
    .pause(3000.milliseconds)



  setUp(scn.inject(atOnceUsers(users = 100))).protocols(httpProtocol)
}
