package scalata.infrastructure.view.jline

object HelpView:
  val helpText: String = "\n" +
    """[W/A/S/D]  Move
      |[Undo]     Go back to previous state
      |[C]        Attack (choose direction: [N/S/E/W])
      |[I]        Interact (choose direction: [N/S/E/W])
      |[U]        Use item (specify item name)
      |[Q]        Quit""".stripMargin + "\n"
