package de.richardtreier.demo.dao

import de.richardtreier.demo.dao.schema.Tables
import de.richardtreier.demo.model.NovelCreateDto
import org.jooq.DSLContext
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Novel db mutations
 */
@ApplicationScoped
class NovelMutations {
  @Inject
  lateinit var dsl: DSLContext

  fun insertNovel(novelCreateDto: NovelCreateDto): Long {
    val novelRecord = dsl.newRecord(Tables.NOVEL)
    novelRecord.title = novelCreateDto.title
    novelRecord.insert()

    val chapters = novelCreateDto.chapters.mapIndexed { chapterIndex, chapterDto ->
      val chapter = dsl.newRecord(Tables.CHAPTER)
      chapter.index = chapterIndex
      chapter.title = chapterDto.title
      chapter.novelId = novelRecord.id
      chapter
    }
    dsl.batchInsert(chapters).execute()

    return novelRecord.id
  }
}
