package scalata.domain.util

import alice.tuprolog.*
import scalata.domain.util.Geometry.Point2D

/** Thin helper around <i>tuProlog</i> that:
 * <ul>
 * <li>adds Scala → Prolog implicit conversions (<code>String</code>, <code>Seq</code>);</li>
 * <li>provides extractors (<code>asInt</code>, <code>asPoint</code>);</li>
 * <li>builds a reusable Prolog engine with arbitrary clauses.</li>
 * </ul>
 */
object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)

  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

  /** Down-cast a Prolog numeric term to Scala <code>Int</code>. */
  def asInt(t: Term): scala.Int =
    t.asInstanceOf[alice.tuprolog.Number].intValue

  /** Extract a <code>pos(X,Y)</code> struct to a Scala <code>Point2D</code>. */
  def asPoint(t: Term): Point2D =
    Point2D(
      asInt(t.asInstanceOf[Struct].getArg(0).getTerm),
      asInt(t.asInstanceOf[Struct].getArg(1).getTerm)
    )

  /** Build a Prolog engine seeded with <code>clauses</code>.
   * Returns a function that, given a goal term, yields a lazy stream of
   * <code>SolveInfo</code> solutions.
   */
  def mkPrologEngine(clauses: String*): Term => LazyList[SolveInfo] =
    val engine = Prolog()
    engine.setTheory(Theory(clauses.mkString(" ")))
    goal =>
      LazyList.unfold(Option(engine.solve(goal))):
        case Some(info) if info.isSuccess =>
          val next =
            if info.hasOpenAlternatives then Some(engine.solveNext()) else None
          Some(info -> next)
        case _ => None
