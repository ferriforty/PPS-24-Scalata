package scalata.infrastructure.cli.view

trait GameView:
  def textToDisplay: String = ""

  def display(): Unit =
    this.clearScreen()
    println(textToDisplay)

  def getInput: String = scala.io.StdIn.readLine().trim

  def displayError(message: String): Unit = println(s"❌ Error: $message")

  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")
