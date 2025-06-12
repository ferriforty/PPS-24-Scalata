package scalata.infrastructure.cli.view

object MenuView extends GameView:

  override def display(): Unit =
    this.clearScreen()
    println(" -> Welcome to DungeonUnderYou <-")
    println(" --> This Menu is useless <--")
    println(" --> Do you want to play? <--")
    println(" ---> [y/n] <---")
