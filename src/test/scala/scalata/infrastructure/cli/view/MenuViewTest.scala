package scalata.infrastructure.cli.view

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MenuViewTest extends AnyFlatSpec with Matchers:

  "MenuView" should "display the main menu correctly" in:
    val menuText = MenuView.textToDisplay

    menuText should include(" -> Welcome to Scalata <-")
    menuText should include(" --> This Menu is useless <--")
    menuText should include(" --> Do you want to play? <--")
    menuText should include(" ---> [y/n] <---")
