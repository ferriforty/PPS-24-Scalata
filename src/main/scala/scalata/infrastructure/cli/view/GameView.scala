package scalata.infrastructure.cli.view

trait GameView:
  val textToDisplay: String = ""

  def display(text: String = textToDisplay): Unit =
    this.clearScreen()
    println(text)

  def getInput: String = scala.io.StdIn.readLine().trim

  def displayError(message: String): Unit = println(s"❌ Error: $message")

  def clearScreen(): Unit = print("\u001b[2J\u001b[H")
