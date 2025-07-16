package scalata.domain.world

import cats.data.NonEmptyList
import cats.syntax.all.*
import scalata.domain.util.GameError

/** Immutable wrapper that couples a [[World]] with its [[GameState]] and keeps
  * an <i>undo history</i>.
  *
  * <h4>Fields</h4> <ul> <li><b>world</b> current game world (rooms, player,
  * difficulty).</li> <li><b>gameState</b> progress on the current run.</li>
  * <li><b>history</b> non-empty list of previous snapshots, newest first, used
  * for the <code>undo</code> feature.</li> </ul>
  *
  * All “update” methods return a <u>new</u> <code>GameSession</code>; nothing
  * is mutated in place.
  */
final case class GameSession(
    world: World,
    gameState: GameState,
    history: NonEmptyList[(World, GameState)]
):

  def getWorld: World = this.world

  def getGameState: GameState = this.gameState

  private def getSession: (World, GameState) =
    (this.getWorld, this.getGameState)

  def updateWorld(world: World): GameSession = copy(world = world)

  def updateGameState(gameState: GameState): GameSession =
    copy(gameState = gameState)

  /** Push current snapshot onto the history stack. */
  def store: GameSession =
    copy(history = NonEmptyList(getSession, history.toList))

  /** Undo the last stored snapshot; if none left, add an <code>UndoError</code>
    * note and keep the current state.
    */
  def undo: GameSession =
    history.tail.toNel.fold(
      this.updateGameState(
        this.getGameState.withNote(GameError.UndoError().message)
      )
    ): t =>
      copy(
        world = t.head._1,
        gameState = t.head._2,
        history = t
      )

object GameSession:
  /** Create a new session with an empty history containing the initial
    * snapshot.
    */
  def init(world: World, state: GameState): GameSession =
    GameSession(world, state, NonEmptyList.one((world, state)))
