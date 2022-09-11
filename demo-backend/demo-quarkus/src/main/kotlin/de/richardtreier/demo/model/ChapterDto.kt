package de.richardtreier.demo.model

import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
  val id: Long,
  val title: String
)
