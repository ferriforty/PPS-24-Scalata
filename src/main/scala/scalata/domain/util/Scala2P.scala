package scalata.domain.util

import alice.tuprolog.*

object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

  def extractTerm(t: Term, i: Integer): Term =
    t.asInstanceOf[Struct].getArg(i).getTerm

  def mkPrologEngine(clauses: String*): Term => LazyList[Term] =
    val engine = Prolog()
    engine.setTheory(Theory(clauses mkString " "))

    goal =>
      LazyList.unfold(engine.solve(goal)): info =>
        if !info.isSuccess then None
        else
          val term     = info.getSolution
          val nextInfo =
            if info.hasOpenAlternatives then engine.solveNext()
            else engine.solve("fail.")
          Some(term -> nextInfo)
