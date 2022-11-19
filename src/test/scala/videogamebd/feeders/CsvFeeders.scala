package videogamebd.feeders

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._


class CsvFeeders extends Simulation{

  val httpProtocol = http.baseUrl(url = "https://videogamedb.uk/api")
    .acceptHeader(value = "application/json")

  val csvFeeders= csv("data/gameCsvFile.csv").circular

  def getSpecifcVideoGame() = {
    repeat(10) {
      feed(csvFeeders)
        .exec(http("get video game with name - #{gameName}")
        .get("/videogame/#{gameId}")
        .check(jsonPath("$.name").is("#{gameName}"))
        .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("Csv feeder test")
    .exec(getSpecifcVideoGame())

  setUp(scn.inject(atOnceUsers(users = 1))
  ).protocols(httpProtocol)
}
