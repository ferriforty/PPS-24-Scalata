package scalata.domain.util

sealed trait GameResult[+T]

object GameResult:
  case class Success[T](value: T, message: Option[String] = None)
      extends GameResult[T]
  case class Error(error: GameError, message: String)
      extends GameResult[Nothing]

  def success[T](value: T): GameResult[T] = Success(value)
  def error(error: GameError, message: String): GameResult[Nothing] =
    Error(error, message)
