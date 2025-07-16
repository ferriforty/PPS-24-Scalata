package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.infrastructure.view.View

/** **Game over screen**.
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
class GameOverView[F[_]: Sync](
    val view: GameView[F, String]
) extends View[GameOverView[F], String, Boolean, String, F](view):

  override protected def banner: String =
    """================ GAME OVER ================
      |
      |Do you want to go back to the main menu? y/n
      |""".stripMargin

  override protected def parse(raw: String): Option[Boolean] =
    Shared.booleanParse(raw)
