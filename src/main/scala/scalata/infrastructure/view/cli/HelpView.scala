package scalata.infrastructure.view.cli

object HelpView:
  val helpText: String = "\n" +
    """[W/A/S/D]  Move
      |[Undo]     Go back to previous state
      |[A]        Attack (choose direction: [N/S/E/W])
      |[I]        Interact (choose direction: [N/S/E/W])
      |[U]        Use item (specify item name)
      |[Q]        Quit""".stripMargin + "\n"
