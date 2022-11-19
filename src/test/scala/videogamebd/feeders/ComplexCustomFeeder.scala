package videogamebd.feeders

import io.gatling.core.Predef.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._

import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter
import scala.util.Random

class ComplexCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  var idNumbers = (1 to 10).iterator
  val rnd = new Random()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }


  def getRandomDate(startDate: LocalDate, random: Random) = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  val scn = scenario("Complex Custom Feeder")


  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(length = 5)),
    "releaseDate" -> getRandomDate(now, rnd),
    "reviewScore" -> rnd.nextInt(100),
    "category" -> ("Category-" + randomString(6)),
    "rating" -> ("Rating-" + randomString(4))
  ))

  def authenticate() = {
    exec(http("Authenticate")
      .post("/authenticate")
      .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))

  }

  def createNewGame() = {
    repeat(10) {
      feed(customFeeder)
        .exec(http("Create New game - #{name}")
          .post("/videogame")
          .header("Authorization", "Bearer #{jwtToken}")
          .body(ElFileBody("bodies/newGameTemplate.json")).asJson
          .check(bodyString.saveAs("responseBody")))
        .exec { session => println(session("responseBody").as[String]); session }
        .pause(1)

    }
  }

  val snc = scenario("Complex Custom Feeder")
    .exec(authenticate())
    .exec(createNewGame())

  //Un usuario simultaneo
  setUp(snc.inject(atOnceUsers(users = 1))
  ).protocols(httpProtocol)


  //rampuser de 10 usuarios durante 10 segundos
  setUp(scn.inject(nothingFor(5),
    atOnceUsers(5),
    rampUsers(10).during(10)).protocols(httpProtocol))

  //Usuarios constantes durante 10sg
  setUp(scn.inject(nothingFor(5),
    constantUsersPerSec(10).during(10))
    .protocols(httpProtocol))


}
