package scalata.infrastructure.cli.view

object MenuView extends GameView:

  override def textToDisplay: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin
