package de.richardtreier.demo

import de.richardtreier.demo.dao.NovelMutations
import de.richardtreier.demo.dao.NovelQueries
import de.richardtreier.demo.model.NovelCreateDto
import de.richardtreier.demo.model.NovelDetailPageDto
import de.richardtreier.demo.model.NovelDto
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.validation.Valid

/**
 * Novel CRUD Service
 */
@ApplicationScoped
class NovelService {
  @Inject
  lateinit var novelQueries: NovelQueries

  @Inject
  lateinit var novelMutations: NovelMutations

  fun listNovels(): List<NovelDto> {
    return novelQueries.listNovels()
  }

  fun novelDetailPage(novelId: Long): NovelDetailPageDto {
    return novelQueries.novelDetailPageById(novelId)
  }

  fun createNovel(@Valid novelCreateDto: NovelCreateDto): NovelDto {
    val novelId = novelMutations.insertNovel(novelCreateDto)
    return novelQueries.novelById(novelId)
  }

}
