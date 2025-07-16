package scalata.domain.entities

/** Base type for any in-game object that must be identifiable.
  *
  * <ul> <li><b>id</b> – unique key, stable for the whole run</li>
  * <li><b>name</b> – human-readable label</li> </ul>
  */
trait Entity:
  val id: String
  val name: String
