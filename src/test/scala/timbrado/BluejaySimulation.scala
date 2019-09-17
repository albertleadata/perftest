package timbrado

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BluejaySimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://bluejaydev")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.contentTypeHeader("application/json; charset=utf-8")
		.userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val uri2 = "http://timbrado:8080/timbrado/portal"

	val scn = scenario("BluejaySimulation")
		.exec(http("alpha")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "alpha")
			.formParam("Go", "Go"))
		.pause(2)
		.exec(http("delta")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "delta")
			.formParam("Go", "Go"))
		.pause(1)
		.exec(http("beta")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "beta")
			.formParam("Go", "Go"))
		.pause(3)
		.exec(http("theta")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "theta")
			.formParam("Go", "Go"))
		.pause(1)
		.exec(http("alpha")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "alpha")
			.formParam("Go", "Go"))

	setUp(scn.inject(atOnceUsers(2),rampUsers(4) during (6 seconds),constantUsersPerSec(4) during (2 minutes))).protocols(httpProtocol)
}
