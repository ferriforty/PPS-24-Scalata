package scalata.infrastructure.view

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView
import scalata.infrastructure.view.terminal.Shared

trait View[V <: View[V, C, O, I, F], C, O, I, F[_]: Sync](view: GameView[F, I]):

  protected def banner: C

  protected def parse(raw: I): Option[O]

  def ask: F[O] =
    for
      _ <- view.display(banner)
      res <- loop
    yield res

  private def loop: F[O] = Shared.run(view, parse)
