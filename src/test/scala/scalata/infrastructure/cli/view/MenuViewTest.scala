package scalata.infrastructure.cli.view

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.io.ByteArrayOutputStream

class MenuViewTest extends AnyFlatSpec with Matchers:

  "MenuView" should "display the main menu correctly" in:
    val output = new ByteArrayOutputStream()

    Console.withOut(output):
      MenuView.display()


    val printedOutput = output.toString
    printedOutput should include(" -> Welcome to DungeonUnderYou <-")
    printedOutput should include(" --> This Menu is useless <--")
    printedOutput should include(" --> Do you want to play? <--")
    printedOutput should include(" ---> [y/n] <---")

  "MenuView" should "display error messages correctly" in:
    val output = new ByteArrayOutputStream()
    val errorMessage = "Invalid selection"

    Console.withOut(output):
      MenuView.displayError(errorMessage)


    val printedOutput = output.toString
    printedOutput should include("❌ Error: Invalid selection")

