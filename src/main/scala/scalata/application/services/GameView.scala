package scalata.application.services

import cats.syntax.all.*

/** Abstract UI bridge.
  *
  * The trait is effect-polymorphic: every action yields a value in the
  * effect&nbsp;<code>F[_]</code> (e.g.&nbsp;<code>IO</code>).
  *
  * @tparam F
  *   effect type constructor (<code>cats.effect.IO</code>, etc.)
  * @tparam I
  *   raw input representation (e.g.&nbsp;<code>String</code>, scan-coded key,
  *   structured AST)
  */
trait GameView[F[_], I]:

  /** Render text or any printable value.
    *
    * Implementations decide where to display: • console (`println`) • Swing
    * text area • web socket, etc.
    *
    * @param text
    *   content to render; `A`’s `toString` is used if no specific formatting is
    *   applied.
    */
  def display[A](text: A): F[Unit]

  /** Block (or suspend) until the next player command is available.
    *
    * Implementations **must**: • return an empty/default value if the input
    * source is closed; • guarantee that reads are safe to call repeatedly; •
    * suspend in `F` rather than performing side effects eagerly.
    */
  def getInput: F[I]

  /** Show an error message to the player.
    *
    * Recommended to prefix the text with `"Error: "` or apply red colour, but
    * exact styling is up to the concrete adapter.
    */
  def displayError[A](message: A): F[Unit]

  /** Clear the entire screen / viewport.
    *
    * On a terminal implementation this is usually an ANSI escape sequence; on
    * Swing it might reset a text area; on web it may send a “clear” event.
    */
  def clearScreen: F[Unit]
