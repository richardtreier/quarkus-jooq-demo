package de.richardtreier.demo

import de.richardtreier.demo.model.NovelCreateDto
import de.richardtreier.demo.model.NovelDetailPageDto
import de.richardtreier.demo.model.NovelDto
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Novel API
 *
 * Demo: Simple REST API with Quarkus + Kotlin Serialization
 */
@Path("/novels")
class NovelResource {
  @Inject
  lateinit var novelService: NovelService

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  fun listNovels(): List<NovelDto> {
    return novelService.listNovels()
  }

  @GET
  @Path("/{novelId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  fun novelDetailPage(@PathParam("novelId") novelId: Long): NovelDetailPageDto {
    return novelService.novelDetailPage(novelId)
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  fun createNovel(novel: NovelCreateDto): NovelDto {
    return novelService.createNovel(novel)
  }
}
