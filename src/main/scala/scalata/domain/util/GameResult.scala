package scalata.domain.util

sealed trait GameResult[+T]

/** ADT representing the outcome of a domain operation.
  *
  * <h4>Cases</h4> <ul> <li><code>Success[T]</code> – carries a value of type
  * <code>T</code> and an optional user-facing message.</li>
  * <li><code>Error</code> – wraps a non-recoverable
  * <code>GameError</code>.</li> </ul>
  *
  * Helper constructors <code>success</code> and <code>error</code> make
  * call-sites concise.
  */
object GameResult:

  /** Successful outcome.
    *
    * @param value
    *   payload returned by the operation
    * @param message
    *   optional note to show in the UI (help text, info, …)
    */
  case class Success[T](value: T, message: Option[String] = None)
      extends GameResult[T]

  /** Failure outcome carrying a domain-level error. */
  case class Error(error: GameError) extends GameResult[Nothing]

  /** Convenience factory for <code>Success</code>. */
  def success[T](value: T, message: Option[String] = None): GameResult[T] =
    Success(value, message)

  /** Convenience factory for <code>Error</code>. */
  def error(error: GameError): GameResult[Nothing] =
    Error(error)
