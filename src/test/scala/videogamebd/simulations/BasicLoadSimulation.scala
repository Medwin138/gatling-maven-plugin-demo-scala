package videogamebd.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.core.structure
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class BasicLoadSimulation extends Simulation{

  val httpProtocol = http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value = "application/json")

  def getAllVideogames() = {
    //repetir este proceso con un contador parametrizado en este ej de 0 a 4
    repeat(5,counterName = "counter") {
      exec(http(requestName = "Get Specific game with id: #{counter}")
        .get("/videogame/#{counter}")
        .check(status.in(200 to 201)))
    }
  }


  def getSpecificGame() = {
    //repetir esta consulta 3 veces
    repeat(3){
      exec(http(requestName = "Get all games")
        .get("/videogame/")
        .check(status.is(200)))
    }}

  //Reutilizacion de codigo atraves de metodos "def"

  val scn = scenario(name = "Code reuse")
    .exec(getAllVideogames())
    .pause(5)
    .exec(getSpecificGame())
    .repeat(1) {
      pause(5)
        .exec(getAllVideogames())
    }

  //Usuarios constantes durante 10sg
  setUp(scn.inject(nothingFor(5),
    constantUsersPerSec(10).during(10))
    .protocols(httpProtocol))

}
