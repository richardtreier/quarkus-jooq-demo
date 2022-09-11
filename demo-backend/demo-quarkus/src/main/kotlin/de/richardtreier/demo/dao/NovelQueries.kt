package de.richardtreier.demo.dao

import de.richardtreier.demo.dao.schema.Tables
import de.richardtreier.demo.model.ChapterDto
import de.richardtreier.demo.model.NovelDetailPageDto
import de.richardtreier.demo.model.NovelDto
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL.count
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Novel db queries
 */
@ApplicationScoped
class NovelQueries {
  @Inject
  lateinit var dsl: DSLContext
  fun listNovels(): List<NovelDto> {
    return queryNovels()
  }

  fun novelById(novelId: Long): NovelDto {
    return queryNovels(byId = novelId).firstOrNull() ?: throwNotFound(novelId)
  }

  private fun queryNovels(byId: Long? = null): List<NovelDto> {
    val n = Tables.NOVEL
    val c = Tables.CHAPTER

    val filters = mutableListOf<Condition>()
    if (byId != null) {
      filters.add(n.ID.eq(byId))
    }

    return dsl.select(
      n.ID,
      n.TITLE,
      count(c.ID).`as`("numChapters")
    )
      .from(n)
      .leftJoin(c).on(c.NOVEL_ID.eq(n.ID))
      .where(filters)
      .groupBy(n.ID)
      .fetchInto(NovelDto::class.java)
  }

  fun novelDetailPageById(novelId: Long): NovelDetailPageDto {
    val n = Tables.NOVEL
    val c = Tables.CHAPTER

    return dsl.select(
      n.ID,
      n.TITLE,
      multiset(
        select(c.ID, c.TITLE).from(c)
          .where(c.NOVEL_ID.eq(n.ID))
          .orderBy(c.INDEX)
      ).`as`("chapters").convertFrom { it.into(ChapterDto::class.java) }
    )
      .from(n)
      .fetchInto(NovelDetailPageDto::class.java)
      .firstOrNull() ?: throwNotFound(novelId)
  }

  private fun throwNotFound(novelId: Long): Nothing {
    error("Could not find novel with id $novelId")
  }
}
