package scalata.infrastructure.view.terminal.jline

import cats.effect.{Resource, Sync}
import cats.syntax.all.*
import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.utils.NonBlockingReader
import scalata.application.services.GameView

final class JLineView[F[_] : Sync] private(
                                            private val term: Terminal,
                                            private val reader: NonBlockingReader
                                          ) extends GameView[F, String]:

  private val keys = Set('w', 'a', 's', 'd', 'W', 'A', 'S', 'D')

  def display[A](text: A): F[Unit] =
    clearScreen >> Sync[F].blocking(term.writer.println(text))

  def getInput: F[String] =
    Sync[F].blocking:
      val out = term.writer
      val line = new StringBuilder

      def echo(c: Char): Unit =
        out.print(c)
        out.flush()

      var ch: Int = reader.read()
      echo(ch.toChar)

      if keys.contains(ch.toChar) then ch.toChar.toString
      else
        while ch != -1 && ch != '\n' && ch != '\r' do
          line.append(ch.toChar)
          ch = reader.read()
          echo(ch.toChar)
          if ch == 127 || ch == 8 then
            if line.nonEmpty then
              line.deleteCharAt(line.length - 1)
              out.print("\b \b")
              out.flush()
            ch = reader.read()
        line.toString.trim

  def displayError[A](msg: A): F[Unit] =
    Sync[F].blocking(term.writer.println(s"Error: $msg"))

  def clearScreen: F[Unit] =
    Sync[F].blocking(term.puts(org.jline.utils.InfoCmp.Capability.clear_screen))

  private[view] def closeTerminal: F[Unit] =
    Sync[F].blocking(term.close())

object JLineView:
  def apply[F[_] : Sync](): Resource[F, GameView[F, String]] =
    resource[F]

  private def resource[F[_] : Sync]: Resource[F, GameView[F, String]] =
    Resource.make(
      Sync[F].delay:
        val term = TerminalBuilder.terminal()
        val reader = term.reader()
        new JLineView[F](term, reader)
    )(view => view.closeTerminal)
