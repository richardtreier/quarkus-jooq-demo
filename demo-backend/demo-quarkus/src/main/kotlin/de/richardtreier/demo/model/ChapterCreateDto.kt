package de.richardtreier.demo.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank

@Serializable
data class ChapterCreateDto(
  @field:NotBlank
  val title: String
)
