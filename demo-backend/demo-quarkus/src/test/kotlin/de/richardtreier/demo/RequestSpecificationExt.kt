package de.richardtreier.demo

import io.restassured.specification.RequestSpecification
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Currently RestAssured requires one of the classical serialization APIs on the classpath.
 *
 * So we feed [RequestSpecification] the serialized JSON as string.
 */
inline fun <reified T> RequestSpecification.bodyKt(obj: T): RequestSpecification {
  return body(Json.encodeToString(obj))
}
