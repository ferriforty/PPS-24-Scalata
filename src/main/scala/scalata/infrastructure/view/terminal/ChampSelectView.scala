package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.domain.util.PlayerClasses
import scalata.infrastructure.view.View

/** **Champion-selection screen**.
 *
 * The view extends the generic [[scalata.infrastructure.view.View View]]
 * template, supplying:
 * • an ASCII banner that lists available possible inputs;
 * • a `parse` implementation that maps raw user input.
 *
 * @param view low-level [[scalata.application.services.GameView GameView]]
 *             through which all rendering and input occur (dependency-injected
 *             so that we stay UI-agnostic).
 * @tparam F effect type (e.g. `IO`)
 */
class ChampSelectView[F[_]: Sync](
    val view: GameView[F, String]
) extends View[ChampSelectView[F], String, PlayerClasses, String, F](view):

  override protected def banner: String =
    """ -> Let's create your champion now <-
      | --> What is your play style? <--
      | ---> [M]age (ranged)   <---
      | ---> [B]arbarian (melee)<---
      | ---> [A]ssassin (stealth)<---""".stripMargin

  override protected def parse(raw: String): Option[PlayerClasses] =
    raw.trim.toLowerCase match
      case "m" => Some(PlayerClasses.Mage)
      case "b" => Some(PlayerClasses.Barbarian)
      case "a" => Some(PlayerClasses.Assassin)
      case _   => None
