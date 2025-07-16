package scalata.domain.entities.components

import scalata.domain.entities.items.{Dust, Potion, Weapon}
import scalata.domain.entities.{Entity, Item}

import scala.compiletime.{summonFrom, summonInline}

/** Type-class describing an item that can be **used** by an entity.
 *
 * <h4>Contract</h4>
 * <ul>
 * <li><b>A</b> – concrete item subtype.</li>
 * <li><b>E</b> – entity subtype that can own / use the item.</li>
 * <li><code>use</code> returns a <u>new</u> instance of the owner
 * reflecting the item’s effect (pure, no mutation).</li>
 * </ul>
 */
trait Usable[A <: Item, E <: Entity]:
  def use(item: A, owner: E): E

/** Inline helper that tries to use a runtime item if a matching
 * <code>Usable</code> instance is available.
 *
 * <p>The method performs an inline pattern-match on the concrete item
 * type and, through <code>summonInline</code>, obtains the given
 * instance for <code>Potion</code>, <code>Dust</code> or
 * <code>Weapon</code>.
 * Unsupported items yield <code>None</code>.</p>
 */
object Usable:

  inline def tryUse[E <: Entity](item: Item, owner: E): Option[E] =
    item match
      case p: Potion => Some(summonInline[Usable[Potion, E]].use(p, owner))
      case d: Dust   => Some(summonInline[Usable[Dust, E]].use(d, owner))
      case w: Weapon => Some(summonInline[Usable[Weapon, E]].use(w, owner))
      case _         => None
