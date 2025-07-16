package scalata.domain.util

import alice.tuprolog.*
import scalata.domain.util.Geometry.Point2D

object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)

  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

  def asInt(t: Term): scala.Int =
    t.asInstanceOf[alice.tuprolog.Number].intValue

  def asPoint(t: Term): Point2D =
    Point2D(
      asInt(t.asInstanceOf[Struct].getArg(0).getTerm),
      asInt(t.asInstanceOf[Struct].getArg(1).getTerm)
    )

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
