package scalata.infrastructure.cli.view

object MenuView extends GameView:

  override val textToDisplay: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin
