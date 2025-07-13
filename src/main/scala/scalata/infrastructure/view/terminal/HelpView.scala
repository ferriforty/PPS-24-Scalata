package scalata.infrastructure.view.terminal

object HelpView:
  val helpText: String = "\n" +
    """[W/A/S/D]  Move
      |[Undo]     Go back to previous state
      |[C]        Combat (choose direction: [Space] [N/S/E/W])
      |[I]        Interact (choose direction: [Space] [N/S/E/W])
      |[U]        Use item (specify item name)
      |[Q]        Quit""".stripMargin + "\n"
