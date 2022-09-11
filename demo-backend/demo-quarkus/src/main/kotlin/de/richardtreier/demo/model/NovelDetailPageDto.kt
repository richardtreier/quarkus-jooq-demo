package de.richardtreier.demo.model

import kotlinx.serialization.Serializable

@Serializable
data class NovelDetailPageDto(
  val id: Long,
  val title: String,
  val chapters: List<ChapterDto>
)
