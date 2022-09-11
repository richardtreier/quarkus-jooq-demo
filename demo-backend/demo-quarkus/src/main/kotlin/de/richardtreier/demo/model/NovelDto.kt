package de.richardtreier.demo.model

import kotlinx.serialization.Serializable

@Serializable
data class NovelDto(
  val id: Long,
  val title: String,
  val numChapters: Int
)
