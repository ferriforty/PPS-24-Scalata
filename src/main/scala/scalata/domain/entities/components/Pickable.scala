package scalata.domain.entities.components

trait Pickable[E <: Pickable[E]]:
  def pick(): Unit
