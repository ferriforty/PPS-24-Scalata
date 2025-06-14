package scalata.infrastructure.cli

import scalata.infrastructure.cli.controller.GameEngine

@main
def CliApp(): Unit = GameEngine().gameLoop()
