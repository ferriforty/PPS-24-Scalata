package scalata.application.services

/** Generic <i>factory type-class</i> able to build domain entities.
 *
 * <h4>Type parameters</h4>
 * <ul>
 * <li><b>F</b> – concrete factory that implements the trait
 * (self-type used for fluent APIs).</li>
 * <li><b>E</b> – entity returned by the factory (e.g.&nbsp;<code>Enemy</code>,
 * <code>Item</code>).</li>
 * <li><b>P</b> – descriptor specifying which concrete subtype to create
 * (enum, string tag, config object …).</li>
 * </ul>
 *
 * Implementations are pure: calling <code>create</code> must not mutate shared
 * state but simply return a fresh instance of <b>E</b>.
 */
trait EntityFactory[F <: EntityFactory[F, E, P], E, P]:

  /** Produce a new entity of the requested kind.
   *
   * @param entityType discriminator telling the factory which subtype to build
   * @param id         stable identifier; empty string lets the factory
   *                   auto-generate one
   * @return a brand-new instance of <b>E</b>
   */
  def create(entityType: P, id: String = ""): E
