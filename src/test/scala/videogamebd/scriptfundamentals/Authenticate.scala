package videogamebd.scriptfundamentals

import io.gatling.core.Predef.Simulation
import io.gatling.http.Predef.http
import io.gatling.core.Predef._
import io.gatling.core.structure
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class Authenticate extends Simulation{

  val httpProtocol = http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value = "application/json")
    .contentTypeHeader("application/json")


  def autenticate()={
    exec(http("Autenticate")
    .post("/authenticate")
    .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
    .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewGame()={
    exec(http("Create New game")
      .post("/videogame")
      .header("Authorization","Bearer #{jwtToken}")
      .body(StringBody(
        "{\n  \"category\": \"Platform\",\n  \"name\": \"Mario\",\n  \"rating\": \"Mature\",\n  \"releaseDate\": \"2012-05-04\",\n  \"reviewScore\": 85\n}"

      )))
  }
val scn = scenario("Authenticate")
  .exec(autenticate())
  .exec(createNewGame())

  setUp(scn.inject(atOnceUsers(users = 1))
  ).protocols(httpProtocol)

}
