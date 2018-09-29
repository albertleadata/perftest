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

    val uri1 = "http://bluejaydev/index.php"
    val uri2 = "http://canarydev:8080/timbrado/portal"

	val scn = scenario("BluejaySimulation")
		.exec(http("request_0")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0000_request.txt")))
		.pause(3)
		.exec(http("request_1")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0001_request.txt")))
		.pause(2)
		.exec(http("request_2")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "alpha")
			.formParam("Go", "Go")
			.resources(http("request_3")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0003_request.txt"))))
		.pause(3)
		.exec(http("request_4")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0004_request.txt")))
		.pause(1)
		.exec(http("request_5")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "delta")
			.formParam("Go", "Go"))
		.pause(1)
		.exec(http("request_6")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0006_request.txt"))
			.resources(http("request_7")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "gamma")
			.formParam("Go", "Go")))
		.pause(2)
		.exec(http("request_8")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0008_request.txt"))
			.resources(http("request_9")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "zeta")
			.formParam("Go", "Go")))
		.pause(1)
		.exec(http("request_10")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0010_request.txt"))
			.resources(http("request_11")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "epsilon")
			.formParam("Go", "Go")))
		.pause(1)
		.exec(http("request_12")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0012_request.txt")))
		.pause(1)
		.exec(http("request_13")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "beta")
			.formParam("Go", "Go"))
		.pause(1)
		.exec(http("request_14")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0014_request.txt")))
		.pause(3)
		.exec(http("request_15")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0015_request.txt")))
		.pause(3)
		.exec(http("request_16")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0016_request.txt"))
			.resources(http("request_17")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "theta")
			.formParam("Go", "Go")))
		.pause(1)
		.exec(http("request_18")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0018_request.txt"))
			.resources(http("request_19")
			.post(uri2 + "")
			.headers(headers_2)
			.formParam("op", "alpha")
			.formParam("Go", "Go")))
		.pause(2)
		.exec(http("request_20")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0020_request.txt")))
		.pause(3)
		.exec(http("request_21")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0021_request.txt")))
		.pause(3)
		.exec(http("request_22")
			.post("/index.php?ctx=api")
			.body(RawFileBody("BluejaySimulation_0022_request.txt")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}