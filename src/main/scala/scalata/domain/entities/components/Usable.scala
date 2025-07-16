package scalata.domain.entities.components

import scalata.domain.entities.items.{Dust, Potion, Weapon}
import scalata.domain.entities.{Entity, Item}

import scala.compiletime.{summonFrom, summonInline}

trait Usable[A <: Item, E <: Entity]:
  def use(item: A, owner: E): E

object Usable:

  inline def tryUse[E <: Entity](item: Item, owner: E): Option[E] =
    item match
      case p: Potion => Some(summonInline[Usable[Potion, E]].use(p, owner))
      case d: Dust   => Some(summonInline[Usable[Dust, E]].use(d, owner))
      case w: Weapon => Some(summonInline[Usable[Weapon, E]].use(w, owner))
      case _         => None
