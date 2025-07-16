package scalata.infrastructure.view

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView
import scalata.infrastructure.view.terminal.Shared

/** Generic view helper.
 *
 * <ul>
 * <li><b>banner</b> – value shown before asking.</li>
 * <li><b>parse</b> – turns raw input into a domain object.</li>
 * <li><b>ask</b> – prints banner, then delegates to a loop that
 * re-prompts via <code>Shared.run</code> until parsing succeeds.</li>
 * </ul>
 */
trait View[V <: View[V, C, O, I, F], C, O, I, F[_]: Sync](view: GameView[F, I]):

  protected def banner: C

  protected def parse(raw: I): Option[O]

  /** Show banner once, then enter retry loop. */
  def ask: F[O] =
    for
      _ <- view.display(banner)
      res <- loop
    yield res

  private def loop: F[O] = Shared.run(view, parse)
