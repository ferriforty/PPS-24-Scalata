package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.domain.util.PlayerClasses
import scalata.infrastructure.view.View

class ChampSelectView[F[_]: Sync, I](
    val view: GameView[F, I]
) extends View[ChampSelectView[F, I], String, PlayerClasses, I, F](view):

  override protected def banner: String =
    """ -> Let's create your champion now <-
      | --> What is your play style? <--
      | ---> [M]age (ranged)   <---
      | ---> [B]arbarian (melee)<---
      | ---> [A]ssassin (stealth)<---""".stripMargin

  override protected def parse(raw: I): Option[PlayerClasses] =
    raw.toString.trim.toLowerCase match
      case "m" => Some(PlayerClasses.Mage)
      case "b" => Some(PlayerClasses.Barbarian)
      case "a" => Some(PlayerClasses.Assassin)
      case _   => None
