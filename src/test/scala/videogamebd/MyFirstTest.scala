package videogamebd

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstTest extends Simulation{

  val httpProtocol=http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader(value = "application/json")

  // Scenario Definition
  val scn =scenario(name = "My firs Test")
    .exec(http(requestName = "Get all games")
    .get("/videogame"))

setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpProtocol)

}
