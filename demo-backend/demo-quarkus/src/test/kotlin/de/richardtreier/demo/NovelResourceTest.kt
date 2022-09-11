package de.richardtreier.demo

import de.richardtreier.demo.model.ChapterCreateDto
import de.richardtreier.demo.model.NovelCreateDto
import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

@QuarkusTest
class NovelResourceTest {

  @Test
  @TestTransaction
  fun testNovelList() {
    given()
      .`when`()
      .accept(ContentType.JSON)
      .get("/novels")
      .then()
      .statusCode(200)
      .body("", hasSize<Any>(1))
      .body("[0].title", equalTo("Against The Gods"))
      .body("[0].numChapters", equalTo(3))
  }

  @Test
  @TestTransaction
  fun testNovelDetail() {
    given()
      .`when`()
      .accept(ContentType.JSON)
      .get("/novels/100")
      .then()
      .statusCode(200)
      .body("title", equalTo("Against The Gods"))
      .body("chapters", hasSize<Any>(3))
      .body("chapters.title", contains("ATG Chapter 1", "ATG Chapter 2", "ATG Chapter 3"))
  }

  @Test
  @TestTransaction
  fun testCreateNovel() {
    given()
      .`when`()
      .accept(ContentType.JSON)
      .contentType(ContentType.JSON)
      .bodyKt(
        NovelCreateDto(
          "The Legendary Moonlight Sculptor",
          listOf(ChapterCreateDto("LMS Ch 1"), ChapterCreateDto("LMS Ch 2"))
        )
      )
      .post("/novels")
      .then()
      .statusCode(200)
      .body("title", equalTo("The Legendary Moonlight Sculptor"))
      .body("numChapters", equalTo("2"))


    given()
      .`when`()
      .accept(ContentType.JSON)
      .get("/novels")
      .then()
      .statusCode(200)
      .body("", hasSize<Any>(2))
  }

  @Test
  @TestTransaction
  fun testCreateNovelWithBadTitle() {
    given()
      .`when`()
      .accept(ContentType.JSON)
      .contentType(ContentType.JSON)
      .bodyKt(
        NovelCreateDto(
          "",
          listOf(ChapterCreateDto("LMS Ch 1"), ChapterCreateDto("LMS Ch 2"))
        )
      )
      .post("/novels")
      .then()
      .statusCode(500)
  }

  @Test
  @TestTransaction
  fun testCreateNovelWithBadChapterTitle() {
    given()
      .`when`()
      .accept(ContentType.JSON)
      .contentType(ContentType.JSON)
      .bodyKt(
        NovelCreateDto(
          "The Legendary Moonlight Sculptor",
          listOf(ChapterCreateDto(""), ChapterCreateDto("LMS Ch 2"))
        )
      )
      .post("/novels")
      .then()
      .statusCode(500)
  }

}
