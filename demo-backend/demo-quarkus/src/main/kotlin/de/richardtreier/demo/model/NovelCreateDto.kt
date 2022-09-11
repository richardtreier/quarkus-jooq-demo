package de.richardtreier.demo.model

import kotlinx.serialization.Serializable
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Serializable
data class NovelCreateDto(
  @field:NotBlank
  val title: String,

  @field:NotEmpty
  @field:Valid
  val chapters: List<ChapterCreateDto>
)
