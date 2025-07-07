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
      new Iterable[Term]:
        override def iterator: Iterator[Term] = new Iterator[Term]:
          var solution: SolveInfo = engine.solve(goal);

          override def hasNext: Boolean =
            solution != null && (solution.isSuccess || solution.hasOpenAlternatives)

          override def next(): Term =
            val current = solution.getSolution
            solution = if solution.hasOpenAlternatives then
              engine.solveNext()
            else null
            current
    .to(LazyList)