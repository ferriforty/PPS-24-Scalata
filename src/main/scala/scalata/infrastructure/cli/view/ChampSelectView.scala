package scalata.infrastructure.cli.view

object ChampSelectView extends GameView:

  override def textToDisplay: String =
    """ -> Let 's create your champion now <-
      | --> What is your play style? <--
      | ---> [M]age(ranged) <---
      | ---> [B]arbarian(melee) <---
      | ---> [A]ssassin(stealth) <---""".stripMargin
