package scalata.domain.util

/** Lightweight 2-D integer geometry using an <i>opaque type</i>.
 *
 * <ul>
 * <li><b>Point2D</b> is represented as a tuple <code>(x, y)</code> but the
 * tuple is hidden outside the module.</li>
 * <li>Convenience ops: Manhattan distance, 4-neighbour listing,
 * axis-aligned range and vector addition.</li>
 * </ul>
 */
object Geometry:
  opaque type Point2D = (Int, Int)

  object Point2D:
    /** Construct a point. */
    inline def apply(x: Int, y: Int): Point2D = (x, y)

    /** Pattern-match extractor: <code>case Point2D(x, y)</code>. */
    def unapply(p: Point2D): Some[(Int, Int)] = Some((p._1, p._2))

  extension (p: Point2D)

    def x: Int = p._1
    def y: Int = p._2

    /** Manhattan distance to another point. */
    def distanceTo(to: Point2D): Int =
      math.abs(p.x - to.x) + math.abs(p.y - to.y)

    /** Four orthogonal neighbours. */
    private def neighbours: List[Point2D] =
      List(
        Point2D(p.x + 1, p.y),
        Point2D(p.x - 1, p.y),
        Point2D(p.x, p.y + 1),
        Point2D(p.x, p.y - 1)
      )

    /** Neighbours that satisfy a predicate. */
    def neighboursFiltered(f: Point2D => Boolean): List[Point2D] =
      neighbours.filter(f)

    /** All integer points in the axis-aligned rectangle from this to <code>dest</code>. */
    def rangeTo(dest: Point2D): List[Point2D] =
      (for
        i <- if p.x <= dest.x then p.x to dest.x else p.x to dest.x by -1
        j <- if p.y <= dest.y then p.y to dest.y else p.y to dest.y by -1
        if i > 0 && j > 0
      yield Point2D(i, j)).toList

    /** Translate the point by a delta vector. */
    def moveBy(delta: Point2D): Point2D =
      Point2D(p.x + delta.x, p.y + delta.y)
